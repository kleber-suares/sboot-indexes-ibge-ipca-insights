package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.queries;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IpcaHistoryQueryService {

    private final MongoTemplate mongoTemplate;

    private static final String INDICATOR_NAME = "IPCA - Variação mensal";
    private static final String UNIT_OF_MEASURE = "%";

    public IpcaHistoryQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<IpcaHistoryDataEntity> findByPeriodRange(
        int startYear,
        int startMonth,
        int endYear,
        int endMonth) {

        int startPeriod = startYear * 100 + startMonth;
        int endPeriod = endYear * 100 + endMonth;

        Query query = new Query();

        query.addCriteria(
            Criteria.where("indicatorName").is(INDICATOR_NAME)
        );

        query.addCriteria(
            Criteria.where("referencePeriodCode")
                .gte(String.valueOf(startPeriod))
                .lte(String.valueOf(endPeriod))
        );

        query.addCriteria(
            Criteria.where("unitOfMeasureLabel").is(UNIT_OF_MEASURE)
        );

        query
            .with(org.springframework.data.domain.Sort.by("referencePeriodCode")
                .ascending());

        return mongoTemplate.find(query, IpcaHistoryDataEntity.class);
    }
}