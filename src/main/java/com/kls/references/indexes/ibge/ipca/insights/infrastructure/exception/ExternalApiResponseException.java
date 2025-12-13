package com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception;

public class ExternalApiResponseException extends WebClientOperationException {

    public ExternalApiResponseException (String message) {
        super(message);
    }

    public ExternalApiResponseException (String message, Throwable throwable) {
        super(message, throwable);
    }
}
