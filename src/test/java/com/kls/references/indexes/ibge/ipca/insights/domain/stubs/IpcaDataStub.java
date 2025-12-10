package com.kls.references.indexes.ibge.ipca.insights.domain.stubs;

import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IpcaDataStub {

    private IpcaDataStub() {}

    public static List<IpcaData> getAllIpcaDataList() {
        List<IpcaData> ipcaDataList = new ArrayList<>();

        ipcaDataList.addAll(getIpcaHistoryDataList());
        ipcaDataList.add(getIpcaDataMetadata());
        ipcaDataList.add(getIpcaNonHistoryData());

        return ipcaDataList;
    }

    public static List<IpcaData> getIpcaInfoDataList() {
        return List.of(getIpcaDataMetadata(), getIpcaDataMetadata());
    }

    public static List<IpcaData> getIpcaHistoryDataList() {
        List<IpcaData> ipcaDataList = new ArrayList<>();
        Random random = new Random();
        int min = 0;
        int max = 10;

        for (int i = 0; i < 10; i++) {
            IpcaData ipcaData = new IpcaData();
            ipcaData.setIndicatorCode("63");
            ipcaData.setIndicatorName("IPCA - Variação mensal");
            ipcaData.setInflationRate(String.valueOf(random.nextDouble(max - min) - min));
            min = 1985;
            max = 2024;
            ipcaData.setReferencePeriodCode(String.valueOf(random.nextInt(max - min) - min));
            ipcaData.setReferencePeriodLabel("janeiro " + ipcaData.getReferencePeriodCode());
            ipcaData.setRegionCode("1");
            ipcaData.setRegionName("Brasil");
            ipcaData.setTerritorialLevelCode("1");
            ipcaData.setTerritorialLevelName("Brasil");
            ipcaData.setUnitOfMeasureCode("2");

            if(i % 2 == 0) {
                ipcaData.setUnitOfMeasureLabel("Número-índice");
            } else {
                ipcaData.setUnitOfMeasureLabel("%");
            }

            ipcaDataList.add(ipcaData);
        }

        return ipcaDataList;
    }



    private static IpcaData getIpcaDataMetadata() {
        IpcaData ipcaData = new IpcaData();

        ipcaData.setIndicatorCode("Variável (Código)");
        ipcaData.setIndicatorName("Variável");
        ipcaData.setInflationRate("Valor");
        ipcaData.setReferencePeriodCode("Mês (Código)");
        ipcaData.setReferencePeriodLabel("Mês");
        ipcaData.setRegionCode("Brasil (Código)");
        ipcaData.setRegionName("Brasil");
        ipcaData.setTerritorialLevelCode("Nível Territorial (Código)");
        ipcaData.setTerritorialLevelName("Nível Territorial");
        ipcaData.setUnitOfMeasureCode("Unidade de Medida (Código)");
        ipcaData.setUnitOfMeasureLabel("Unidade de Medida");

        return ipcaData;
    }

    private static IpcaData getIpcaNonHistoryData() {
        IpcaData ipcaData = new IpcaData();

        ipcaData.setIndicatorCode("63");
        ipcaData.setIndicatorName("IPCA - Variação mensal");
        ipcaData.setInflationRate("...");
        ipcaData.setReferencePeriodCode("197912");
        ipcaData.setReferencePeriodLabel("dezembro 1979");
        ipcaData.setRegionCode("1");
        ipcaData.setRegionName("Brasil");
        ipcaData.setTerritorialLevelCode("1");
        ipcaData.setTerritorialLevelName("Brasil");
        ipcaData.setUnitOfMeasureCode("");
        ipcaData.setUnitOfMeasureLabel("");

        return ipcaData;
    }
}
