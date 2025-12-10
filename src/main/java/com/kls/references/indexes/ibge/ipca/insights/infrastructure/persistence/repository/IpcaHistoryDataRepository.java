package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IpcaHistoryDataRepository extends MongoRepository<IpcaHistoryDataRepository, String> {
}
