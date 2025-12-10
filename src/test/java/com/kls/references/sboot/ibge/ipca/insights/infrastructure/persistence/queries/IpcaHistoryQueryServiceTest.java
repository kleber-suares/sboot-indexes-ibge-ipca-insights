package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.queries;

import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IpcaHistoryQueryServiceTest {

    private MongoTemplate mongoTemplate;
    private IpcaHistoryQueryService queryService;

    @BeforeEach
    void setup() {
        mongoTemplate = mock(MongoTemplate.class);
        queryService = new IpcaHistoryQueryService(mongoTemplate);
    }

    @Test
    void shouldBuildQueryWithCorrectCriteria() {

        int startYear = 2020, startMonth = 1;   // 202001
        int endYear = 2020, endMonth = 12;      // 202012

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);

        when(mongoTemplate.find(any(Query.class), eq(IpcaHistoryDataEntity.class)))
            .thenReturn(List.of());

        queryService.findByPeriodRange(startYear, startMonth, endYear, endMonth);

        verify(mongoTemplate).find(captor.capture(), eq(IpcaHistoryDataEntity.class));

        Query builtQuery = captor.getValue();
        String queryString = builtQuery.getQueryObject().toJson();

        assertThat(queryString)
            .contains("\"indicatorName\": \"IPCA - Variação mensal\"")
            .contains("\"unitOfMeasureLabel\": \"%\"")
            .contains("\"referencePeriodCode\": {\"$gte\": \"202001\", \"$lte\": \"202012\"}");
    }

    @Test
    void shouldReturnResultListFromMongoTemplate() {

        IpcaHistoryDataEntity entity = new IpcaHistoryDataEntity();
        List<IpcaHistoryDataEntity> expected = List.of(entity);

        when(mongoTemplate.find(any(Query.class), eq(IpcaHistoryDataEntity.class)))
            .thenReturn(expected);

        List<IpcaHistoryDataEntity> result =
            queryService.findByPeriodRange(2021, 5, 2021, 7);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(entity);
    }

    @Test
    void shouldApplyAscendingSortOnReferencePeriodCode() {

        ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);

        when(mongoTemplate.find(any(Query.class), eq(IpcaHistoryDataEntity.class)))
            .thenReturn(List.of());

        queryService.findByPeriodRange(2022, 1, 2022, 3);

        verify(mongoTemplate).find(captor.capture(), eq(IpcaHistoryDataEntity.class));

        Query query = captor.getValue();

        assertThat(query.getSortObject().toJson())
            .contains("{\"referencePeriodCode\": 1}"); // ASC = 1
    }

    @Test
    void shouldReturnEmptyListWhenDatabaseFindReturnsEmpty() {

        when(mongoTemplate.find(any(Query.class), eq(IpcaHistoryDataEntity.class)))
            .thenReturn(List.of());

        List<IpcaHistoryDataEntity> result =
            queryService.findByPeriodRange(2019, 1, 2019, 3);

        assertThat(result).isEmpty();
    }
}
