package edu.learning.db.mongodbfs.dto.path;

public record PagedRegexRequest(String regex,
                                int page,
                                int size) {
}
