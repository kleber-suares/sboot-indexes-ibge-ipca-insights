package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.dto;

public record IpcaDataImportResults(
    Integer deletedCount,
    Integer insertedCount,
    Integer updatedCount
) { }
