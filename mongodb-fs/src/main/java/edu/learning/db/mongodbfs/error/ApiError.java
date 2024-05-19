package edu.learning.db.mongodbfs.error;

public record ApiError(String message,
                       int status) {
}
