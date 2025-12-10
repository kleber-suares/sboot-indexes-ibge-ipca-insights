package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImportLogRepository extends MongoRepository<ImportLogEntity, String> {
}
