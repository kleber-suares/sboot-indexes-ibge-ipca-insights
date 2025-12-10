package com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception;

public class ImportOperationException extends RuntimeException {

    public ImportOperationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
