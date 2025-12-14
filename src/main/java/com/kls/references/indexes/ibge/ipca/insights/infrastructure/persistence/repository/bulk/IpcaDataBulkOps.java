package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.bulk;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaDataEntity;
import com.mongodb.bulk.BulkWriteResult;

import java.util.List;

public interface IpcaDataBulkOps<T extends IpcaDataEntity> {

    BulkWriteResult replaceAll(List<T> newDocuments, Class<T> entityClass);

}
