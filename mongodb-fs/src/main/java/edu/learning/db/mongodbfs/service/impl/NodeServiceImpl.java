package edu.learning.db.mongodbfs.service.impl;

import edu.learning.db.mongodbfs.dto.aggregation.AggregationResult;
import edu.learning.db.mongodbfs.dto.aggregation.AggregationType;
import edu.learning.db.mongodbfs.dto.node.MinimalNodeDTO;
import edu.learning.db.mongodbfs.dto.node.NodeDTO;
import edu.learning.db.mongodbfs.dto.path.PagedRegexRequest;
import edu.learning.db.mongodbfs.dto.path.PathInput;
import edu.learning.db.mongodbfs.error.ApiException;
import edu.learning.db.mongodbfs.mapper.NodeMapper;
import edu.learning.db.mongodbfs.model.Node;
import edu.learning.db.mongodbfs.model.NodeType;
import edu.learning.db.mongodbfs.repository.NodeRepository;
import edu.learning.db.mongodbfs.service.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    public NodeServiceImpl(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public NodeDTO getNode(String nodeId) {
        log.info("Get node by id {}", nodeId);
        // {_id: ObjectId('...')}
        Node node = nodeRepository.findById(nodeId).orElseThrow(() -> ApiException.NOT_FOND);
        return NodeMapper.mapToDTO(node);
    }

    @Override
    public NodeDTO getNodeByPath(PathInput pathInput) {
        log.info("Get node by path {}", pathInput);
        // {parentPath: "abc", name: "def"}
        Node node = nodeRepository.findByParentPathAndName(
                pathInput.parentPath(), pathInput.name()
        ).orElseThrow(() -> ApiException.NOT_FOND);
        return NodeMapper.mapToDTO(node);
    }

    @Override
    public List<MinimalNodeDTO> listFolder(String parentPath, int page, int size) {
        log.info("Listing folder {} with paging params: page = {}, size = {}", parentPath, page, size);
        List<Node> nodes = nodeRepository.findByParentPath(parentPath, createPage(page, size));
        return NodeMapper.mapToMinimalDTOList(nodes);
    }

    @Override
    public List<MinimalNodeDTO> findNodesByName(String name, int page, int size) {
        log.info("Listing nodes by name {} with paging params: page = {}, size = {}", name, page, size);
        List<Node> nodes = nodeRepository.findByName(name, createPage(page, size));
        return NodeMapper.mapToMinimalDTOList(nodes);
    }

    @Override
    public List<MinimalNodeDTO> findNodesByNameRegex(PagedRegexRequest pagedRegexRequest) {
        String nameRegex = pagedRegexRequest.regex();
        int page = pagedRegexRequest.page();
        int size = pagedRegexRequest.size();
        log.info("Listing nodes by name regex {} with paging params: page = {}, size = {}", nameRegex, page, size);
        List<Node> nodes = nodeRepository.findByNameRegex(nameRegex, createPage(page, size));
        return NodeMapper.mapToMinimalDTOList(nodes);
    }

    @Override
    public List<NodeDTO> searchFilesByContent(PagedRegexRequest pagedRegexRequest) {
        String contentRegex = pagedRegexRequest.regex();
        int page = pagedRegexRequest.page();
        int size = pagedRegexRequest.size();
        log.info(
                "Listing nodes by storage content regex {} with paging params: page = {}, size = {}",
                contentRegex, page, size
        );
        List<Node> nodes = nodeRepository.findByStorage_contentRegex(contentRegex, createPage(page, size));
        return NodeMapper.mapToDTOList(nodes);
    }

    @Override
    public AggregationResult countNodesInSubtree(String parentPath, NodeType nodeType) {
        log.info("Counting nodes in subtree: parentPath = {}, nodeType = {}", parentPath, nodeType);

        String parentPathRegex = "^" + parentPath;

        int numOfNodes;
        if (nodeType == NodeType.DIR) {
            numOfNodes = nodeRepository.countByParentPathStartsWithAndStorageExists(parentPath, false);
        } else if (nodeType == NodeType.FILE) {
            numOfNodes = nodeRepository.countByParentPathStartsWithAndStorageExists(parentPath, true);
        } else if (nodeType == null) {
            numOfNodes = nodeRepository.countByParentPathStartsWith(parentPath);
        } else {
            throw ApiException.INVALID_NODE_TYPE;
        }

        return AggregationResult.builder()
                .aggregationType(AggregationType.COUNT)
                .parentPathExpr(parentPath)
                .value(numOfNodes)
                .build();
    }

    @Override
    public AggregationResult countLargerFilesInSubtree(String parentPathPrefix, int threshold) {
        String parentPathRegex = "^" + parentPathPrefix;
        log.info("Counting larger nodes in subtree: parentPathRegex = {}, threshold = {}", parentPathRegex, threshold);

        return AggregationResult.builder()
                .aggregationType(AggregationType.COUNT)
                .parentPathExpr(parentPathPrefix)
                .value(nodeRepository.countFileInSubtreeLargerThanThreshold(parentPathRegex, threshold))
                .build();
    }

    @Override
    public NodeDTO getLargestFileInSubtree(String parentPathPrefix) {
        String parentPathRegex = "^" + parentPathPrefix;
        log.info("Getting the largest node in subtree: parentPathRegex = {}", parentPathRegex);
        Node largestFile = nodeRepository.findLargestFileInSubtree(parentPathRegex).orElse(null);
        return NodeMapper.mapToDTO(largestFile);
    }

    @Override
    public AggregationResult getTotalFileSizeInSubtree(String parentPathPrefix) {
        log.info("Getting the total size of the files in subtree: parentPathRegex = {}", parentPathPrefix);
        String parentPathRegex = "^" + parentPathPrefix;
        long totalSize = nodeRepository.getTotalSizeInSubtree(parentPathRegex);
        return AggregationResult.builder()
                .aggregationType(AggregationType.SUM)
                .parentPathExpr(parentPathPrefix)
                .value(totalSize)
                .build();
    }

    private PageRequest createPage(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "_id"));
    }
}
