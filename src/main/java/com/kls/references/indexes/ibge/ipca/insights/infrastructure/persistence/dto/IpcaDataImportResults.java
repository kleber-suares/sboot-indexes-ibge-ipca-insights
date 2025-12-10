package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto;

public record IpcaDataImportResults(
    Integer deletedCount,
    Integer insertedCount,
    Integer updatedCount
) { }
