package edu.learning.db.mongodbfs.dto.aggregation;

import lombok.Builder;

@Builder
public record AggregationResult(String parentPathExpr,
                                long value,
                                AggregationType aggregationType) {
}
