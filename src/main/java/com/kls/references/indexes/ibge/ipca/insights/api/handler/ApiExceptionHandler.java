package com.kls.references.indexes.ibge.ipca.insights.api.handler;

import com.kls.references.indexes.ibge.ipca.insights.api.response.ApiErrorResponse;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        String msg = "An internal error ocurred while processing the request.";

        log.error(msg, e);

        ApiErrorResponse apiErrorResponse =
            new ApiErrorResponse(msg);

        return ResponseEntity
            .internalServerError()
            .body(apiErrorResponse);
    }

    @ExceptionHandler(ExternalApiResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleExternalApiResponseException(ExternalApiResponseException e) {
        String msg = "The external service didn't return a valid response.";

        log.error(msg, e);

        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(new ApiErrorResponse(msg));
    }

    @ExceptionHandler(ExternalApiTimeoutException.class)
    public ResponseEntity<ApiErrorResponse> handleExternalApiTimeoutException(ExternalApiTimeoutException e) {
        String msg = "The request was aborted: The external service took too long to respond and the operation has timed out.";

        log.error(msg, e);

        return ResponseEntity
            .status(HttpStatus.GATEWAY_TIMEOUT)
            .body(new ApiErrorResponse(msg));
    }

}
