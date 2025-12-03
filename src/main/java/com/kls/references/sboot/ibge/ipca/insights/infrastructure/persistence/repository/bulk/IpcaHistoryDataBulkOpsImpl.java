package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.bulk;

import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IpcaHistoryDataBulkOpsImpl implements IpcaHistoryDataBulkOps {

    private final MongoTemplate mongoTemplate;

    public IpcaHistoryDataBulkOpsImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public BulkWriteResult replaceAll(List newDocuments, Class entityClass) {
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
