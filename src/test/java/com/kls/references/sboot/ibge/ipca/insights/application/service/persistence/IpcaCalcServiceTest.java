package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.api.response.CumulativeInflationRateResponse;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.queries.IpcaHistoryQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpcaCalcServiceTest {

    @Mock
    private IpcaHistoryQueryService queryService;

    @InjectMocks
    private IpcaCalcService service;

    private IpcaHistoryDataEntity entity(String period, String rate) {
        IpcaHistoryDataEntity e = new IpcaHistoryDataEntity();
        e.setReferencePeriodCode(period);
        e.setInflationRate(new BigDecimal(rate));
        return e;
    }

    @Test
    void shouldReturnEmptyResponseWhenNoDataFound() {
        when(queryService.findByPeriodRange(2020, 1, 2020, 3))
            .thenReturn(List.of());

        CumulativeInflationRateResponse r =
            service.calculateCumulativeInflation(2020, 1, 2020, 3);

        assertThat(r.cumulativeInflationRate()).isNull();
        assertThat(r.numberFormat()).isNull();
        assertThat(r.noOfPeriodsConsidered()).isNull();
        assertThat(r.startPeriod()).isNull();
        assertThat(r.endPeriod()).isNull();
    }

    @Test
    void shouldCalculateCumulativeRateCorrectly() {
        // 202001: 0.50%
        // 202002: 0.30%
        // fator = (1 + 0.0050) * (1 + 0.0030)
        // taxa acumulada ~ 0.802%
        List<IpcaHistoryDataEntity> data = List.of(
            entity("202001", "0.50"),
            entity("202002", "0.30")
        );

        when(queryService.findByPeriodRange(2020, 1, 2020, 2))
            .thenReturn(data);

        CumulativeInflationRateResponse r =
            service.calculateCumulativeInflation(2020, 1, 2020, 2);

        assertThat(r.cumulativeInflationRate())
            .isNotNull()
            .isEqualByComparingTo(new BigDecimal("0.8015"));

        assertThat(r.noOfPeriodsConsidered()).isEqualTo(2);
        assertThat(r.startPeriod()).isEqualTo(202001);
        assertThat(r.endPeriod()).isEqualTo(202002);
        assertThat(r.numberFormat()).isEqualTo("%");
    }

    @Test
    void shouldAdjustStartAndEndPeriodsBasedOnDatabaseValues() {

        // Requested: 202001 → 202003
        // Database has: 202002 → 202003
        List<IpcaHistoryDataEntity> data = List.of(
            entity("202002", "0.40"),
            entity("202003", "0.20")
        );

        when(queryService.findByPeriodRange(2020, 1, 2020, 3))
            .thenReturn(data);

        CumulativeInflationRateResponse r =
            service.calculateCumulativeInflation(2020, 1, 2020, 3);

        assertThat(r.startPeriod()).isEqualTo(202002);
        assertThat(r.endPeriod()).isEqualTo(202003);
        assertThat(r.noOfPeriodsConsidered()).isEqualTo(2);
    }
}