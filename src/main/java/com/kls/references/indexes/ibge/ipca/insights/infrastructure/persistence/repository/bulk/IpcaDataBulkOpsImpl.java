package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.bulk;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaDataEntity;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IpcaDataBulkOpsImpl<T extends IpcaDataEntity> implements IpcaDataBulkOps<T> {

    private final MongoTemplate mongoTemplate;

    public IpcaDataBulkOpsImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public BulkWriteResult replaceAll(List<T> newDocuments, Class<T> entityClass) {
        BulkOperations bulkOps =
            mongoTemplate.bulkOps(
                BulkOperations.BulkMode.ORDERED,
                entityClass
            );

        bulkOps.remove(new Query());

        bulkOps.insert(newDocuments);

        return bulkOps.execute();
    }
}
