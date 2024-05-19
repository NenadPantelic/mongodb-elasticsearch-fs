package edu.learning.db.mongodbfs.error;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final String message;
    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }


    public static final ApiException BAD_REQUEST = new ApiException("Bad request.", 400);
    public static final ApiException INVALID_NODE_TYPE = new ApiException("Invalid node type.", 400);
    public static final ApiException NOT_FOND = new ApiException("Not found.", 404);
    public static final ApiException INTERNAL_SERVER_ERROR = new ApiException("Internal server error.", 500);
}
