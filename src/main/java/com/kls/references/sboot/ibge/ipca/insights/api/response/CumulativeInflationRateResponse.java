package com.kls.references.sboot.ibge.ipca.insights.api.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.config.FourDecimalBigDecimalSerializer;

import java.math.BigDecimal;

public record CumulativeInflationRateResponse(
    @JsonSerialize(using = FourDecimalBigDecimalSerializer.class)
    BigDecimal cumulativeInflationRate,
    String numberFormat,
    Long noOfPeriodsConsidered,
    Integer startPeriod,
    Integer endPeriod
) {}
