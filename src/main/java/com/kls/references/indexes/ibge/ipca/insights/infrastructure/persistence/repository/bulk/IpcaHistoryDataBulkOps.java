package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.bulk;

import com.mongodb.bulk.BulkWriteResult;

import java.util.List;

public interface IpcaHistoryDataBulkOps<T> {

    BulkWriteResult replaceAll(List<T> newDocuments, Class<T> entityClass);

}
