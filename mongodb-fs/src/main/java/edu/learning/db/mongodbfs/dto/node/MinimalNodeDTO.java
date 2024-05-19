package edu.learning.db.mongodbfs.dto.node;

public record MinimalNodeDTO(String id,
                             String parentPath,
                             String name,
                             String type) {
}
