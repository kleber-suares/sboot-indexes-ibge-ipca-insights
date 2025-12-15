package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.constants.DocumentCollections;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStatus;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import com.kls.references.indexes.ibge.ipca.insights.util.CustomDurationFormatter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;

@Document(collection = DocumentCollections.IMPORT_LOG_COLLECTION)
@TypeAlias("ImportLog")
public class ImportLogEntity {

    @Id
    private String id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private OperationType operationType;
    private ImportStatus status;
    private ImportStage stage;
    private String durationText;
    private Long durationMillis;
    private String sourceDataName;
    private String sourceDataLocation;
    private String errorMessage;
    private Integer deletedCount;
    private Integer insertedCount;
    private Integer updatedCount;

    public ImportLogEntity(
        OperationType operationType,
        ImportStatus status,
        ImportStage stage,
        String sourceDataName,
        String sourceDataLocation
    ) {
        this.operationType = operationType;
        this.status = status;
        this.stage = stage;
        this.sourceDataName = sourceDataName;
        this.sourceDataLocation = sourceDataLocation;
        this.startDateTime = LocalDateTime.now();
        setDurationFields();
    }

    /* *** Getters *** */

    public String getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public ImportStage getStage() {
        return stage;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getDurationText() {
        return this.durationText;
    }

    public Long getDurationMillis() {
        return this.durationMillis;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setDeletedCount(Integer deletedCount) {
        this.deletedCount = deletedCount;
    }

    public void setInsertedCount(Integer insertedCount) {
        this.insertedCount = insertedCount;
    }

    public void setUpdatedCount(Integer updatedCount) {
        this.updatedCount = updatedCount;
    }

    /* *** Setters *** */

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        this.setDurationFields();
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public void setStage(ImportStage stage) {
        this.stage =stage;
    }

    public String getSourceDataName() {
        return sourceDataName;
    }

    public String getSourceDataLocation() {
        return sourceDataLocation;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Integer getDeletedCount() {
        return deletedCount;
    }

    public Integer getInsertedCount() {
        return insertedCount;
    }

    public Integer getUpdatedCount() {
        return updatedCount;
    }

    /* *** Others *** */

    private void setDurationFields() {
        if (startDateTime == null && endDateTime == null) {
            this.durationText = "Start and end time not set";
            this.durationMillis = null;
            return;
        }

        if (startDateTime == null) {
            this.durationText = "Start time not set";
            this.durationMillis = null;
            return;
        }

        if (endDateTime == null) {
            this.durationText = "End time not set";
            this.durationMillis = null;
            return;
        }

        Duration calculatedDuration = Duration.between(startDateTime, endDateTime);

        this.durationText = CustomDurationFormatter.formatFrom(calculatedDuration);
        this.durationMillis = calculatedDuration.toMillis();
    }

}
