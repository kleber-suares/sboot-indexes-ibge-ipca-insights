package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.exception.ImportOperationException;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.mapper.IpcaDataEntityMapper;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.bulk.IpcaHistoryDataBulkOpsImpl;
import com.kls.references.sboot.ibge.ipca.insights.util.CustomDurationFormatter;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.kls.references.sboot.ibge.ipca.insights.infrastructure.config.AsyncConfig.IPCA_IMPORT_EXECUTOR;

@Component
@Slf4j
public class IpcaDataImportImpl implements IpcaDataImport {
    
    private final IpcaHistoryDataBulkOpsImpl bulkOpsImpl;

    public IpcaDataImportImpl(IpcaHistoryDataBulkOpsImpl bulkOpsImpl) {
        this.bulkOpsImpl = bulkOpsImpl;
    }

    @Override
    @Async(IPCA_IMPORT_EXECUTOR)
    public CompletableFuture<Void> importIpcaHistoryData(List<IpcaData> ipcaHistoryValueList) {
        try {
            log.info("Starting import with bulk strategy...");

            long startTime = System.currentTimeMillis();

            var historyDataEntityList = IpcaDataEntityMapper.mapHistoryData(ipcaHistoryValueList);
            log.info("Number of items mapped to persist: {}", historyDataEntityList.size());

            BulkWriteResult bulkWriteResult = bulkOpsImpl.replaceAll(historyDataEntityList, IpcaHistoryDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            //TODO: Persistir BulkWriteResult em uma tabela de log
            log.info("bulkWriteResult deleted: {}", bulkWriteResult.getDeletedCount());
            log.info("bulkWriteResult inserted: {}", bulkWriteResult.getInsertedCount());
            log.info("bulkWriteResult modified: {}", bulkWriteResult.getModifiedCount());

            log.info("Completed import process of IPCA Info Data. Loaded {} records to MongoDB in {}", historyDataEntityList.size(), duration);

        } catch (Exception e) {
            String msg = "Error while processing bulk import of IPCA History Data.";
            log.error(msg, e);
            throw new ImportOperationException(msg , e);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async(IPCA_IMPORT_EXECUTOR)
    public CompletableFuture<Void> importIpcaInfoData(List<IpcaData> ipcaInfoValueList) {
        log.info("Starting import with bulk strategy...");

        try {
            long startTime = System.currentTimeMillis();

            var infoDataEntityList = IpcaDataEntityMapper.mapInfoData(ipcaInfoValueList);
            log.info("Number of items mapped to persist: {}", infoDataEntityList.size());

            BulkWriteResult bulkWriteResult = bulkOpsImpl.replaceAll(infoDataEntityList, IpcaInfoDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            //TODO: Persistir BulkWriteResult em uma tabela de log
            log.info("bulkWriteResult deleted: {}", bulkWriteResult.getDeletedCount());
            log.info("bulkWriteResult inserted: {}", bulkWriteResult.getInsertedCount());
            log.info("bulkWriteResult modified: {}", bulkWriteResult.getModifiedCount());

            log.info("Completed import process of IPCA Info Data. Loaded {} records to MongoDB in {}", infoDataEntityList.size(), duration);

        } catch (Exception e) {
            String msg = "Error while processing bulk import of IPCA Info Data.";
            log.error(msg, e);
            throw new ImportOperationException(msg , e);
        }

        return CompletableFuture.completedFuture(null);
    }

}
