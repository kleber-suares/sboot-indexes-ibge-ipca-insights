package com.kls.references.indexes.ibge.ipca.insights.domain.model;

import com.kls.references.indexes.ibge.ipca.insights.domain.enums.IpcaMeasureType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class IpcaData {

    private String territorialLevelCode; //NC
    private String territorialLevelName; //NN
    private String unitOfMeasureCode; //MC
    private String unitOfMeasureLabel; //MN
    private String inflationRate; //V

    private String regionCode; //D1C
    private String regionName; //D1N

    private String indicatorCode; //D2C
    private String indicatorName; //D2N
    private String referencePeriodCode; //D3C
    private String referencePeriodLabel; //D3N

    public boolean isDataHistory() {
        if (getUnitOfMeasureCode() == null || getUnitOfMeasureCode().isBlank() ||
            getUnitOfMeasureLabel() == null || getUnitOfMeasureLabel().isBlank()) {
            return false;
        }

        return IpcaMeasureType.isHistory(getUnitOfMeasureCode(), getUnitOfMeasureLabel());
    }
}
