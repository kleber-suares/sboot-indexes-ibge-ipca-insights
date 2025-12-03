package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaHistoryValue;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaInfoValue;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.mapper.IpcaDataEntityMapper;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.bulk.IpcaHistoryDataBulkOpsImpl;
import com.kls.references.sboot.ibge.ipca.insights.util.CustomDurationFormatter;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class IpcaHistoryImportImpl implements IpcaHistoryImport {
    
    private final IpcaHistoryDataBulkOpsImpl bulkOpsImpl;

    public IpcaHistoryImportImpl(IpcaHistoryDataBulkOpsImpl bulkOpsImpl) {
        this.bulkOpsImpl = bulkOpsImpl;
    }

    @Override
    public void importHistoryData(List<IpcaHistoryValue> ipcaHistoryValueList) {
        try {
            log.info("Starting import with bulk strategy...");

            long startTime = System.currentTimeMillis();

            var historyDataEntity = IpcaDataEntityMapper.toHistoryDataEntity(ipcaHistoryValueList);
            log.info("Number of items mapped to persist: {}", historyDataEntity.size());

            BulkWriteResult bulkWriteResult = bulkOpsImpl.replaceAll(historyDataEntity, IpcaHistoryDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            //TODO: Persistir BulkWriteResult em uma tabela de log
            log.info("bulkWriteResult deleted: {}", bulkWriteResult.getDeletedCount());
            log.info("bulkWriteResult inserted: {}", bulkWriteResult.getInsertedCount());
            log.info("bulkWriteResult modified: {}", bulkWriteResult.getModifiedCount());

            log.info("Completed import process. Loaded {} records to MongoDB in {}", historyDataEntity.size(), duration);

        } catch (Exception e) {
            log.error("Error while processing bulk import.", e);
            throw e;
        }
    }

    @Override
    public void importInfoData(List<IpcaInfoValue> ipcaInfoValueList) {
        log.info("Starting import with bulk strategy...");

        try {
            long startTime = System.currentTimeMillis();

            var infoDataEntity = IpcaDataEntityMapper.toInfoDataEntity(ipcaInfoValueList);
            log.info("Number of items mapped to persist: {}", infoDataEntity.size());

            BulkWriteResult bulkWriteResult = bulkOpsImpl.replaceAll(infoDataEntity, IpcaInfoDataEntity.class);

            String duration = CustomDurationFormatter.formatFrom(System.currentTimeMillis() - startTime);

            //TODO: Persistir BulkWriteResult em uma tabela de log
            log.info("bulkWriteResult deleted: {}", bulkWriteResult.getDeletedCount());
            log.info("bulkWriteResult inserted: {}", bulkWriteResult.getInsertedCount());
            log.info("bulkWriteResult modified: {}", bulkWriteResult.getModifiedCount());

            log.info("Completed import process. Loaded {} records to MongoDB in {}", infoDataEntity.size(), duration);

        } catch (Exception e) {
            log.error("Error while processing bulk import.", e);
            throw e;
        }
    }

}
