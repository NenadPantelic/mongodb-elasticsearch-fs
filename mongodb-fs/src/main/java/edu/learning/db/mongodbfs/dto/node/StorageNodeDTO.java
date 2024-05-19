package edu.learning.db.mongodbfs.dto.node;

import lombok.Builder;

@Builder
public record StorageNodeDTO(String content,
                             int size,
                             String md5Checksum,
                             String extension) {
}
