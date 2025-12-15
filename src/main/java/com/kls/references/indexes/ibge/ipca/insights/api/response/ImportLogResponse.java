package com.kls.references.indexes.ibge.ipca.insights.api.response;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStatus;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;

import java.time.LocalDateTime;

public record ImportLogResponse (
    String id,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    OperationType operationType,
    ImportStatus status,
    ImportStage stage,
    String durationText,
    Long durationMillis,
    String sourceDataName,
    String sourceDataLocation,
    String errorMessage,
    Integer deletedCount,
    Integer insertedCount,
    Integer updatedCount
){}
