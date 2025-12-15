package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.indexes.ibge.ipca.insights.application.service.persistence.ImportLogRepositoryService;
import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.mapper.IpcaDataEntityMapper;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportResults;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.bulk.IpcaDataBulkOpsImpl;
import com.kls.references.indexes.ibge.ipca.insights.util.CustomDurationFormatter;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.kls.references.indexes.ibge.ipca.insights.infrastructure.config.AsyncConfig.IPCA_DATA_IMPORT_EXECUTOR;

@Component
@Slf4j
public class IpcaDataImportExecutorImpl implements IpcaDataImportExecutor {
    
    private final IpcaDataBulkOpsImpl<IpcaHistoryDataEntity> historyBulkOps;
    private final IpcaDataBulkOpsImpl<IpcaInfoDataEntity> infoBulkOps;
    private final ImportLogRepositoryService importLogRepositoryService;

    public IpcaDataImportExecutorImpl(
        IpcaDataBulkOpsImpl<IpcaHistoryDataEntity> historyBulkOps,
        IpcaDataBulkOpsImpl<IpcaInfoDataEntity> infoBulkOps,
        ImportLogRepositoryService importLogRepositoryService
    ) {
        this.historyBulkOps = historyBulkOps;
        this.infoBulkOps = infoBulkOps;
        this.importLogRepositoryService = importLogRepositoryService;
    }

    @Override
    @Async(IPCA_DATA_IMPORT_EXECUTOR)
    public CompletableFuture<Void> importIpcaHistoryData(List<IpcaData> ipcaHistoryValueList, String logId) {
        log.info("Starting IPCA history data import...");

        importLogRepositoryService.updateProcessDetails(logId, ImportStage.HISTORY_DATA_BULK_IMPORT);

        try {

            long startTime = System.currentTimeMillis();

            var historyDataEntityList = IpcaDataEntityMapper.mapHistoryData(ipcaHistoryValueList);
            log.info("Number of IPCA history data items mapped to persist: {}", historyDataEntityList.size());

            BulkWriteResult bulkWriteResult = historyBulkOps.replaceAll(historyDataEntityList, IpcaHistoryDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            IpcaDataImportResults importResults = new IpcaDataImportResults(
                bulkWriteResult.getDeletedCount(),
                bulkWriteResult.getInsertedCount(),
                bulkWriteResult.getModifiedCount()
            );

            importLogRepositoryService.updateProcessEndWithStatusCompleted(logId, importResults);

            log.info("Completed import process of IPCA history data. Loaded {} records to MongoDB in {}", historyDataEntityList.size(), duration);

        } catch (Exception e) {
            String msg = "Error while processing bulk import of IPCA history data.";
            log.error(msg, e);
            importLogRepositoryService.updateProcessEndWithStatusFailed(logId, msg);
            throw e;
        }

        return CompletableFuture.completedFuture(null);
    }


    @Override
    @Async(IPCA_DATA_IMPORT_EXECUTOR)
    public CompletableFuture<Void> importIpcaInfoData(List<IpcaData> ipcaInfoValueList, String logId) {
        log.info("Starting IPCA info data import...");

        importLogRepositoryService.updateProcessDetails(logId, ImportStage.INFO_DATA_BULK_IMPORT);

        try {

            long startTime = System.currentTimeMillis();

            var infoDataEntityList = IpcaDataEntityMapper.mapInfoData(ipcaInfoValueList);
            log.info("Number of IPCA info data items mapped to persist: {}", infoDataEntityList.size());

            BulkWriteResult bulkWriteResult = infoBulkOps.replaceAll(infoDataEntityList, IpcaInfoDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            IpcaDataImportResults importResults = new IpcaDataImportResults(
                bulkWriteResult.getDeletedCount(),
                bulkWriteResult.getInsertedCount(),
                bulkWriteResult.getModifiedCount()
            );

            importLogRepositoryService.updateProcessEndWithStatusCompleted(logId, importResults);

            log.info("Completed import process of IPCA info data. Loaded {} records to MongoDB in {}", infoDataEntityList.size(), duration);

        } catch (Exception e) {
            String msg = "Error while processing bulk import of IPCA info data.";
            log.error(msg, e);
            importLogRepositoryService.updateProcessEndWithStatusFailed(logId, msg);
            throw e;
        }

        return CompletableFuture.completedFuture(null);
    }

}
