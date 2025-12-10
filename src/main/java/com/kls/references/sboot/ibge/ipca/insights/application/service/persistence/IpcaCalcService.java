package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.api.response.CumulativeInflationRateResponse;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.entity.IpcaHistoryDataEntity;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.queries.IpcaHistoryQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class IpcaCalcService {

    private final IpcaHistoryQueryService queryService;
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final String NUMBER_FORMAT = "%";

    public IpcaCalcService(IpcaHistoryQueryService queryService) {
        this.queryService = queryService;
    }

    public CumulativeInflationRateResponse calculateCumulativeInflation(
        int startYear,
        int startMonth,
        int endYear,
        int endMonth) {

        int requestedStartPeriod = startYear * 100 + startMonth;
        int requestedEndPeriod = endYear * 100 + endMonth;

        List<IpcaHistoryDataEntity> list =
            queryService.findByPeriodRange(
                startYear, startMonth,
                endYear, endMonth
            );

        if (list.isEmpty()) {
            return new CumulativeInflationRateResponse(
                null,
                null,
                null,

                null,
                null
            );
        }

        int foundStartPeriod = list.stream()
            .map(IpcaHistoryDataEntity::getReferencePeriodCode)
            .map(Integer::valueOf)
            .min(Integer::compareTo)
            .orElse(requestedStartPeriod);

        int foundEndPeriod = list.stream()
            .map(IpcaHistoryDataEntity::getReferencePeriodCode)
            .map(Integer::valueOf)
            .max(Integer::compareTo)
            .orElse(requestedEndPeriod);

        BigDecimal accumulationFactor = BigDecimal.ONE;

        for (IpcaHistoryDataEntity item : list) {
            BigDecimal percentageRate = item.getInflationRate();

            BigDecimal decimalRate = percentageRate.divide(ONE_HUNDRED);

            accumulationFactor =
                accumulationFactor
                    .multiply(BigDecimal.ONE.
                        add(decimalRate)
                    );
        }

        BigDecimal cumulativeRate =
            accumulationFactor
                .subtract(BigDecimal.ONE)
                .multiply(ONE_HUNDRED);

        return new CumulativeInflationRateResponse(
            cumulativeRate.setScale(4, RoundingMode.HALF_UP),
            NUMBER_FORMAT,
            (long) list.size(),
            foundStartPeriod,
            foundEndPeriod
        );
    }

}