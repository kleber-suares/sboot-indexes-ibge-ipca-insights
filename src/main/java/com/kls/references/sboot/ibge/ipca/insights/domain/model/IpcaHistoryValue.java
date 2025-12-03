package com.kls.references.sboot.ibge.ipca.insights.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IpcaHistoryValue {

    private String territorialLevelCode; //NC
    private String territorialLevelName; //NN
    private String unitOfMeasureCode; //MC
    private String unitOfMeasureLabel; //MN
    private BigDecimal inflationRate; //V

    private String regionCode; //D1C
    private String regionName; //D1N

    private String indicatorCode; //D2C
    private String indicatorName; //D2N
    private String referencePeriodCode; //D3C
    private String referencePeriodLabel; //D3N

}
