package com.kls.references.indexes.ibge.ipca.insights.api.request;

import jakarta.validation.constraints.NotNull;

public record CumulativeInflationRateRequest(
    @NotNull Integer  startYear,
    @NotNull Integer  startMonth,
    @NotNull Integer  endYear,
    @NotNull Integer  endMonth
) {}