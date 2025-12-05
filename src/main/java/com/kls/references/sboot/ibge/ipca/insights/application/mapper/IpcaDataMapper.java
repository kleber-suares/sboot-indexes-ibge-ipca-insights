package com.kls.references.sboot.ibge.ipca.insights.application.mapper;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;

import java.util.ArrayList;
import java.util.List;

public class IpcaDataMapper {

    private IpcaDataMapper() {}

    public static List<IpcaData> toDomain(List<IpcaHistorySidraResponse> responseList) {
        List<IpcaData> ipcaDataList = new ArrayList<>();

        responseList
            .forEach(item -> {
                    IpcaData domain = new IpcaData();
                    domain.setTerritorialLevelCode(item.getNc());
                    domain.setTerritorialLevelName(item.getNn());
                    domain.setUnitOfMeasureCode(item.getMc());
                    domain.setUnitOfMeasureLabel(item.getMn());
                    domain.setInflationRate(item.getV());
                    domain.setRegionCode(item.getD1c());
                    domain.setRegionName(item.getD1n());
                    domain.setIndicatorCode(item.getD2c());
                    domain.setIndicatorName(item.getD2n());
                    domain.setReferencePeriodCode(item.getD3c());
                    domain.setReferencePeriodLabel(item.getD3n());
                    ipcaDataList.add(domain);
                });

        return ipcaDataList;
    }

}
