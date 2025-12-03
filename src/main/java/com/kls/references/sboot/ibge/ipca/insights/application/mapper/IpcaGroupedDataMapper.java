package com.kls.references.sboot.ibge.ipca.insights.application.mapper;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaGroupedData;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaHistoryValue;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaInfoValue;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IpcaGroupedDataMapper {

    private IpcaGroupedDataMapper() {}

    public static IpcaGroupedData toDomain(List<IpcaHistorySidraResponse> responseList) {
        IpcaGroupedData domain = new IpcaGroupedData();

        domain.setInfoData(new ArrayList<>());
        domain.setHistoryData(new ArrayList<>());

        mapIpcaGroupedData(responseList, domain);

        return domain;
    }


    public static void mapIpcaGroupedData(
        List<IpcaHistorySidraResponse> responseList,
        IpcaGroupedData domain
    ) {
        responseList
            .forEach(item -> {
                    if (isHistory(item)) {
                        var historyData = mapHistoryData(item);
                        domain.getHistoryData().add(historyData);
                    } else {
                        var infoData = mapInfoData(item);
                        domain.getInfoData().add(infoData);
                    }
                }
            );
    }

    private static IpcaHistoryValue mapHistoryData(IpcaHistorySidraResponse response) {
        IpcaHistoryValue ipcaHistoryValue = new IpcaHistoryValue();

        ipcaHistoryValue.setTerritorialLevelCode(response.getNc());
        ipcaHistoryValue.setTerritorialLevelName(response.getNn());
        ipcaHistoryValue.setUnitOfMeasureCode(response.getMc());
        ipcaHistoryValue.setUnitOfMeasureLabel(response.getMn());
        ipcaHistoryValue.setInflationRate(new BigDecimal(response.getV().trim()));//TODO: ver se necessario validar notacao cientifica
        ipcaHistoryValue.setRegionCode(response.getD1c());
        ipcaHistoryValue.setRegionName(response.getD1n());
        ipcaHistoryValue.setIndicatorCode(response.getD2c());
        ipcaHistoryValue.setIndicatorName(response.getD2n());
        ipcaHistoryValue.setReferencePeriodCode(response.getD3c());
        ipcaHistoryValue.setReferencePeriodLabel(response.getD3n());

        return ipcaHistoryValue;
    }

    private static IpcaInfoValue mapInfoData(IpcaHistorySidraResponse response) {
        IpcaInfoValue ipcaInfoValue = new IpcaInfoValue();

        ipcaInfoValue.setTerritorialLevelCode(response.getNc());
        ipcaInfoValue.setTerritorialLevelName(response.getNn());
        ipcaInfoValue.setUnitOfMeasureCode(response.getMc());
        ipcaInfoValue.setUnitOfMeasureLabel(response.getMn());
        ipcaInfoValue.setInflationRate(response.getV());
        ipcaInfoValue.setRegionCode(response.getD1c());
        ipcaInfoValue.setRegionName(response.getD1n());
        ipcaInfoValue.setIndicatorCode(response.getD2c());
        ipcaInfoValue.setIndicatorName(response.getD2n());
        ipcaInfoValue.setReferencePeriodCode(response.getD3c());
        ipcaInfoValue.setReferencePeriodLabel(response.getD3n());

        return ipcaInfoValue;
    }

    private static boolean isHistory(IpcaHistorySidraResponse item) {
        if (item.getMc() == null || item.getMc().isBlank() ||
            item.getMn() == null || item.getMn().isBlank()) {
            return false;
        }

        return (
            ("30".equals(item.getMc()) && "Número-índice".equalsIgnoreCase(item.getMn())) ||
                ("2".equals(item.getMc()) && "%".equals(item.getMn()))
        );
    }

}
