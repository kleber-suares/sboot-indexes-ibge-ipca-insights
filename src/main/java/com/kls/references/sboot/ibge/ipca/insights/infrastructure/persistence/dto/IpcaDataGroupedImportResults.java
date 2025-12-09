package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.dto;

public record IpcaDataGroupedImportResults (
    IpcaDataImportResults historyImportResults,
    IpcaDataImportResults infoImportResults
) { }
