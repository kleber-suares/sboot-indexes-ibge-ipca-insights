package com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception;

public class ExternalApiTimeoutException extends WebClientOperationException {

    public ExternalApiTimeoutException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
