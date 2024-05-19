package edu.learning.db.mongodbfs.controller;

import edu.learning.db.mongodbfs.dto.aggregation.AggregationResult;
import edu.learning.db.mongodbfs.dto.node.MinimalNodeDTO;
import edu.learning.db.mongodbfs.dto.node.NodeDTO;
import edu.learning.db.mongodbfs.dto.path.PagedRegexRequest;
import edu.learning.db.mongodbfs.dto.path.PathInput;
import edu.learning.db.mongodbfs.model.NodeType;
import edu.learning.db.mongodbfs.service.NodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/fs")
public class NodeController {

    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/nodes/{nodeId}")
    public NodeDTO getNode(@PathVariable("nodeId") String nodeId) {
        log.info("Received a request to fetch node by id.");
        return nodeService.getNode(nodeId);
    }

    @PostMapping("/nodes")
    public NodeDTO getNodeByPath(@RequestBody PathInput pathInput) {
        log.info("Received a request to fetch node by path.");
        return nodeService.getNodeByPath(pathInput);
    }

    @GetMapping("/dirs")
    public List<MinimalNodeDTO> listFolder(@RequestParam(value = "parentPath") String parentPath,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Received a request to list folder.");
        return nodeService.listFolder(parentPath, page, size);
    }

    @GetMapping("/nodes")
    public List<MinimalNodeDTO> listNodesByName(@RequestParam(value = "name") String name,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "100") int size) {
        log.info("Received a request to list nodes by name.");
        return nodeService.findNodesByName(name, page, size);
    }

    @PostMapping("/nodes/regex-name")
    public List<MinimalNodeDTO> findNodesByNameRegex(@RequestBody PagedRegexRequest pagedRegexRequest) {
        log.info("Received a request to find nodes by name regex.");
        return nodeService.findNodesByNameRegex(pagedRegexRequest);
    }

    @PostMapping("/nodes/regex-content")
    public List<NodeDTO> searchFilesByContent(@RequestBody PagedRegexRequest pagedRegexRequest) {
        log.info("Received a request to find nodes by content regex.");
        return nodeService.searchFilesByContent(pagedRegexRequest);
    }

    @GetMapping("/nodes/aggregation/count")
    public AggregationResult countNodesInSubtree(@RequestParam(value = "parentPath") String parentPath,
                                                 @RequestParam(value = "type", required = false) String type) {
        log.info("Received a request to count nodes in subtree.");
        return nodeService.countNodesInSubtree(parentPath, NodeType.fromValue(type));
    }

    @GetMapping("/nodes/aggregation/count-larger-files")
    public AggregationResult countLargerFilesInSubtree(@RequestParam(value = "parentPath") String parentPath,
                                                       @RequestParam(value = "threshold", defaultValue = "32768") int threshold) {
        log.info("Received a request to count larger nodes in subtree.");
        return nodeService.countLargerFilesInSubtree(parentPath, threshold);
    }

    @GetMapping("/nodes/aggregation/largest-file")
    public NodeDTO getLargestFileInSubtree(@RequestParam(value = "parentPath") String parentPath) {
        log.info("Received a request to find the largest file in subtree.");
        return nodeService.getLargestFileInSubtree(parentPath);
    }

    @GetMapping("/nodes/aggregation/total-size")
    public AggregationResult getTotalFileSizeInSubtree(@RequestParam(value = "parentPath") String parentPath) {
        log.info("Received a request to find the total size of the files in subtree.");
        return nodeService.getTotalFileSizeInSubtree(parentPath);
    }

}
