package edu.learning.db.mongodbfs.controller.error;

import edu.learning.db.mongodbfs.error.ApiError;
import edu.learning.db.mongodbfs.error.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<ApiError> handleApiException(ApiException e,
                                                             WebRequest request) {
        return new ResponseEntity<>(
                new ApiError(e.getMessage(), e.getStatusCode()),
                HttpStatusCode.valueOf(e.getStatusCode())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ApiError> handleException(RuntimeException e,
                                                          WebRequest request) {
        log.error("An unhandled error has occurred: {}", e.getMessage(), e);
        String errMessage = ApiException.INTERNAL_SERVER_ERROR.getMessage();
        int statusCode = ApiException.INTERNAL_SERVER_ERROR.getStatusCode();

        return new ResponseEntity<>(
                new ApiError(errMessage, statusCode),
                HttpStatusCode.valueOf(statusCode)
        );
    }

}
