package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaInfoDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IpcaInfoDataRepository extends MongoRepository<IpcaInfoDataEntity, String> {
}
