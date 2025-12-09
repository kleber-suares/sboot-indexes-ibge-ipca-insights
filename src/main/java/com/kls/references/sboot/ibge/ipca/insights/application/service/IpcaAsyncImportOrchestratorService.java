package com.kls.references.sboot.ibge.ipca.insights.application.service;

import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.ImportLogRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.IpcaDataRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.application.service.web.IpcaDataFetcherService;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import com.kls.references.sboot.ibge.ipca.insights.util.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
@Slf4j
public class IpcaAsyncImportOrchestratorService {

    private final ImportLogRepositoryService importLogRepositoryService;
    private final IpcaDataFetcherService dataFetcherService;
    private final IpcaDataRepositoryService repositoryService;

    public IpcaAsyncImportOrchestratorService(
        ImportLogRepositoryService importLogRepositoryService,
        IpcaDataFetcherService dataFetcherService,
        IpcaDataRepositoryService repositoryService
    ) {
        this.importLogRepositoryService = importLogRepositoryService;
        this.dataFetcherService = dataFetcherService;
        this.repositoryService = repositoryService;
    }

    public IpcaDataImportLogIds startAsyncImport() {

        var historyDataImportLog =
            importLogRepositoryService.saveProcessStart(OperationType.IPCA_HISTORY_DATA_IMPORT, ImportStage.REQUEST_ACKNOWLEDGED);
        var infoDataImportLog =
            importLogRepositoryService.saveProcessStart(OperationType.IPCA_INFO_DATA_IMPORT, ImportStage.REQUEST_ACKNOWLEDGED);

        var logIds = new IpcaDataImportLogIds(
          historyDataImportLog.getId(),
          infoDataImportLog.getId()
        );

        log.info("IPCA import jobs started. historyImportLogId={} | infoImportLogId={}",
            historyDataImportLog.getId(), infoDataImportLog.getId());

        runAsyncProcessing(logIds);

        return logIds;
    }

    private void runAsyncProcessing(IpcaDataImportLogIds logIds) {

        String correlationId = CorrelationIdUtil.getCorrelationId();

        Runnable task = () -> {

            CorrelationIdUtil.setCorrelationId(correlationId);

            ImportLogEntity historyLogEntry =
                importLogRepositoryService.findById(logIds.ipcaHistoryDataImportLogId()).orElseThrow();
            ImportLogEntity infoLogEntry =
                importLogRepositoryService.findById(logIds.ipcaInfoDataImportLogId()).orElseThrow();

            try {
                //-----------------------------
                // 1. FETCH external data
                //-----------------------------
                importLogRepositoryService
                    .updateProcessDetails(historyLogEntry.getId(), ImportStage.EXTERNAL_SERVICE_FETCH);
                importLogRepositoryService
                    .updateProcessDetails(infoLogEntry.getId(), ImportStage.EXTERNAL_SERVICE_FETCH);

                var ipcaDataList = dataFetcherService.fetchIpcaData();
                log.info("Data fetched: {} records", ipcaDataList.size());

                //-----------------------------
                // 2. IMPORT into database
                //-----------------------------
                repositoryService.importIpcaData(ipcaDataList, logIds);

                //-----------------------------
                // 3. UPDATE log as SUCCESS
                //-----------------------------
                importLogRepositoryService.updateProcessEndWithStatusCompleted(historyLogEntry.getId());
                importLogRepositoryService.updateProcessEndWithStatusCompleted(infoLogEntry.getId());

                log.info("Job finished successfully. historyLog={} | infoLog={}", historyLogEntry.getId(), infoLogEntry.getId());

            } catch (Exception ex) {
                //-----------------------------
                // 4. UPDATE log as FAILURE
                //-----------------------------
                String errorMsg = "One or more jobs failed during the execution of the import of IPCA data.";

                log.error("Import failed: {}", ex.getMessage(), ex);

                importLogRepositoryService.updateProcessEndWithStatusFailed(historyLogEntry.getId(), errorMsg);
                importLogRepositoryService.updateProcessEndWithStatusFailed(infoLogEntry.getId(), errorMsg);

            } finally {
                CorrelationIdUtil.clearCorrelationId();
            }
        };

        // Runs the job asynchronously
        Schedulers.boundedElastic().schedule(task);
    }

    public Optional<ImportLogEntity> getImportStatus(String jobId) {
        return importLogRepositoryService.findById(jobId);
    }

}
