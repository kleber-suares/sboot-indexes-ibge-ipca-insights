package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IpcaHistoryDataRepository extends MongoRepository<IpcaHistoryDataRepository, String> {
}
