package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.TypeAlias;

@Data
@TypeAlias("IpcaData")
public class IpcaDataEntity {

    private String territorialLevelCode;
    private String territorialLevelName;
    private String unitOfMeasureCode;
    private String unitOfMeasureLabel;

    private String regionCode;
    private String regionName;

    private String indicatorCode;
    private String indicatorName;
    private String referencePeriodCode;
    private String referencePeriodLabel;
}
