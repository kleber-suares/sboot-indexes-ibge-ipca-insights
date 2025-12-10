package com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception;

public class WebClientOperationException extends RuntimeException {

    public WebClientOperationException(String message, Throwable throwable) {
        super(message);
    }

}
