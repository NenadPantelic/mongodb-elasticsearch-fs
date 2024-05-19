package edu.learning.db.mongodbfs.service;

import edu.learning.db.mongodbfs.dto.aggregation.AggregationResult;
import edu.learning.db.mongodbfs.dto.node.MinimalNodeDTO;
import edu.learning.db.mongodbfs.dto.node.NodeDTO;
import edu.learning.db.mongodbfs.dto.path.PagedRegexRequest;
import edu.learning.db.mongodbfs.dto.path.PathInput;
import edu.learning.db.mongodbfs.error.ApiException;
import edu.learning.db.mongodbfs.model.NodeType;

import java.util.List;

public interface NodeService {

    /**
     * Fetches a node by its id.
     *
     * @param nodeId an identifier of the node
     * @return NodeDTO representation if the node exists
     * @throws ApiException NOT_FOUND if the node does not exist.
     */
    NodeDTO getNode(String nodeId);

    /**
     * Fetches a node by its path, i.e. parentPathExpr and name.
     *
     * @param pathInput a full path record that contains parent path and node name
     * @return NodeDTO representation if the node exists
     * @throws ApiException NOT_FOUND if the node does not exist.
     */
    NodeDTO getNodeByPath(PathInput pathInput);

    /**
     * Lists all nodes within the given folder. Results are provided in paged manner.
     *
     * @param parentPath path of the folder
     * @param page       page number
     * @param size       page size
     * @return the list of the nodes
     */
    List<MinimalNodeDTO> listFolder(String parentPath, int page, int size);

    /**
     * Lists all nodes with the given name.
     *
     * @param name node name
     * @param page page number
     * @param size page size
     * @return the list of the nodes
     */
    List<MinimalNodeDTO> findNodesByName(String name, int page, int size);

    /**
     * Gets the list of the nodes returned by regex query by name.
     *
     * @param pagedRegexRequest a paged regex expression matching the name of the node
     * @return the list of the nodes
     */
    List<MinimalNodeDTO> findNodesByNameRegex(PagedRegexRequest pagedRegexRequest);

    /**
     * Searches files by content.
     *
     * @param pagedRegexRequest a paged regex expression matching the content of the file
     * @return the list of the nodes (files)
     */
    List<NodeDTO> searchFilesByContent(PagedRegexRequest pagedRegexRequest);

    /**
     * Counts nodes in subtree by node type. If the node type is null, then all nodes are counted in.
     *
     * @param parentPathPrefix prefix of the subtree that should be processed
     * @param nodeType         NodeType - FILE, DIR or null
     * @return AggregationResult that contains the number of counted nodes
     */
    AggregationResult countNodesInSubtree(String parentPathPrefix, NodeType nodeType);

    /**
     * Counts files larger by size than the provided threshold.
     *
     * @param parentPathPrefix prefix of the subtree that should be processed
     * @param threshold        size threshold
     * @return AggregationResult that contains the number of counted files
     */
    AggregationResult countLargerFilesInSubtree(String parentPathPrefix, int threshold);

    /**
     * Gets the largest file by size in the subtree.
     *
     * @param parentPathPrefix prefix of the subtree that should be processed
     * @return the largest file in the subtree (if exists).
     */
    NodeDTO getLargestFileInSubtree(String parentPathPrefix);

    /**
     * Gets the total file size of the files within the subtree.
     *
     * @param parentPathPrefix prefix of the subtree that should be processed
     * @return AggregationResult that contains the accumulated file of present files
     */
    AggregationResult getTotalFileSizeInSubtree(String parentPathPrefix);


}
