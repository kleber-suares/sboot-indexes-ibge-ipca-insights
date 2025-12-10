package com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception;

public class WebClientOperationTimeoutException extends WebClientOperationException {

    public WebClientOperationTimeoutException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
