package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto;

public record IpcaDataGroupedImportResults (
    IpcaDataImportResults historyImportResults,
    IpcaDataImportResults infoImportResults
) { }
