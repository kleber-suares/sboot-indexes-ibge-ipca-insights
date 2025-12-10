package com.kls.references.indexes.ibge.ipca.insights.api.handler;

import com.kls.references.indexes.ibge.ipca.insights.api.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
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

        return ResponseEntity.internalServerError().body(apiErrorResponse);
    }

}
