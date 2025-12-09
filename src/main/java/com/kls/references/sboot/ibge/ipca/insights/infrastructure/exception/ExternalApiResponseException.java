package com.kls.references.sboot.ibge.ipca.insights.infrastructure.exception;

public class ExternalApiResponseException extends RuntimeException {

    public ExternalApiResponseException (String message) {
        super(message);
    }

    public ExternalApiResponseException (String message, Throwable throwable) {
        super(message, throwable);
    }
}
