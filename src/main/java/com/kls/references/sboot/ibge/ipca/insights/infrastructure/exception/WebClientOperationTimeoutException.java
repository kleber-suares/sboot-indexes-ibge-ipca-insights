package com.kls.references.sboot.ibge.ipca.insights.infrastructure.exception;

public class WebClientOperationTimeoutException extends WebClientOperationException {

    public WebClientOperationTimeoutException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
