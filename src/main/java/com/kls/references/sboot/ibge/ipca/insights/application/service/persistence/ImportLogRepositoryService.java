package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportResults;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.enums.ImportStatus;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.ImportLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ImportLogRepositoryService {

    private final ImportLogRepository importLogRepository;

    public ImportLogRepositoryService(ImportLogRepository  importLogRepository) {
        this.importLogRepository = importLogRepository;
    }

    public Optional<ImportLogEntity> findById(String id) {
        return importLogRepository.findById(id);
    }

    public ImportLogEntity saveProcessStart(OperationType operationType, ImportStage stage) {

        ImportLogEntity importLog = new ImportLogEntity(
            operationType,
            ImportStatus.STARTED,
            stage,
            "IBGE-SIDRA API",
            "Web"
        );

        ImportLogEntity savedLog = importLogRepository.save(importLog);

        log.debug("Process {} (ID:{}) start log information has been saved to the database.",
            savedLog.getOperationLabel(), savedLog.getId());

        return importLogRepository.save(importLog);
    }

    public void updateProcessDetails(String logId, ImportStage stage) {
        importLogRepository.findById(logId).ifPresent(
            importLog -> {
                importLog.setEndDateTime(LocalDateTime.now());
                importLog.setStage(stage);

                importLogRepository.save(importLog);

                log.debug("Process {} (ID:{}) execution log information has been updated to the database.",
                    importLog.getOperationLabel(), importLog.getId());
            });
    }

    public void updateProcessEndWithStatusCompleted(String logId) {
        importLogRepository.findById(logId).ifPresent(
            importLog -> {
                importLog.setStatus(ImportStatus.COMPLETED);
                importLog.setEndDateTime(LocalDateTime.now());

                importLogRepository.save(importLog);

                log.info("Process {} (ID:{}) end log information has been saved to the database with status COMPLETED.",
                    importLog.getOperationLabel(), importLog.getId());
            });
    }

    public void updateProcessEndWithStatusCompleted(String logId, IpcaDataImportResults importResults) {
        importLogRepository.findById(logId).ifPresent(
            importLog -> {
                importLog.setStatus(ImportStatus.COMPLETED);
                importLog.setEndDateTime(LocalDateTime.now());
                importLog.setDeletedCount(importResults == null || importResults.deletedCount() == null ? null : importResults.deletedCount());
                importLog.setInsertedCount(importResults == null || importResults.insertedCount() == null ? null : importResults.insertedCount());
                importLog.setUpdatedCount(importResults == null || importResults.updatedCount() == null ? null : importResults.updatedCount());

                importLogRepository.save(importLog);

                log.info("Process {} (ID:{}) end log information has been saved to the database with status COMPLETED.",
                    importLog.getOperationLabel(), importLog.getId());
            });
    }

    public void updateProcessEndWithStatusFailed(String logId, String errorMsg) {
        importLogRepository.findById(logId).ifPresent(
            importLog -> {
                importLog.setStatus(ImportStatus.FAILED);
                importLog.setEndDateTime(LocalDateTime.now());
                importLog.setErrorMessage(errorMsg);

                importLogRepository.save(importLog);

                log.error("Process ID:{} marked log as FAILED. Reason: {}",
                    importLog.getId(), errorMsg);
        });
    }

}
