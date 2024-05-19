package edu.learning.db.mongodbfs.dto.node;

import lombok.Builder;

import java.time.Instant;

@Builder
public record NodeDTO(String id,
                      String parentPath,
                      String name,
                      StorageNodeDTO storage,
                      Instant createdAt,
                      Instant updatedAt) {
}
