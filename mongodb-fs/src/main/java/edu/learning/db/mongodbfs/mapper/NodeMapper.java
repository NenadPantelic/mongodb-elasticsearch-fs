package edu.learning.db.mongodbfs.mapper;

import edu.learning.db.mongodbfs.dto.node.MinimalNodeDTO;
import edu.learning.db.mongodbfs.dto.node.NodeDTO;
import edu.learning.db.mongodbfs.dto.node.StorageNodeDTO;
import edu.learning.db.mongodbfs.model.Node;
import edu.learning.db.mongodbfs.model.NodeType;
import edu.learning.db.mongodbfs.model.StorageNode;

import java.util.List;
import java.util.stream.Collectors;

public class NodeMapper {

    public static NodeDTO mapToDTO(Node node) {
        if (node == null) {
            return null;
        }

        NodeDTO.NodeDTOBuilder dtoBuilder = NodeDTO.builder();
        dtoBuilder = dtoBuilder.id(node.getId())
                .parentPath(node.getParentPath())
                .name(node.getName())
                .createdAt(node.getCreatedAt())
                .updatedAt(node.getUpdatedAt());

        if (node.getType() == NodeType.FILE) {
            dtoBuilder = dtoBuilder.storage(mapStorage(node.getStorage()));
        }

        return dtoBuilder.build();
    }

    private static StorageNodeDTO mapStorage(StorageNode storageNode) {
        if (storageNode == null) {
            return null;
        }

        return StorageNodeDTO.builder()
                .content(storageNode.getContent())
                .size(storageNode.getSize())
                .md5Checksum(storageNode.getMd5Checksum())
                .extension(storageNode.getExtension())
                .build();
    }

    public static List<MinimalNodeDTO> mapToMinimalDTOList(List<Node> nodes) {
        if (nodes == null) {
            return null;
        }

        return nodes.stream()
                .map(node -> new MinimalNodeDTO(
                                node.getId(), node.getParentPath(), node.getName(), node.getType().name()
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<NodeDTO> mapToDTOList(List<Node> nodes) {
        if (nodes == null) {
            return null;
        }

        return nodes.stream()
                .map(NodeMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
