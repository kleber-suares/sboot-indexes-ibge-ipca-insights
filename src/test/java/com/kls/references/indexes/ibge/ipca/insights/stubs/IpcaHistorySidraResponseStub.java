package com.kls.references.indexes.ibge.ipca.insights.stubs;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IpcaHistorySidraResponseStub {
    
    private IpcaHistorySidraResponseStub() {}
    
    public static List<IpcaHistorySidraResponse> getAllIpcaHistorySidraResponseList() {
        List<IpcaHistorySidraResponse> sidraResponseList = new ArrayList<>();

        sidraResponseList.addAll(getIpcaHistorySidraResponseHistoryDataList());
        sidraResponseList.add(getIpcaHistorySidraResponseMetadata());
        sidraResponseList.add(getIpcaHistorySidraResponseNonHistoryData());

        return sidraResponseList;
    }

    public static List<IpcaHistorySidraResponse> getIpcaHistorySidraResponseInfoDataList() {
        return List.of(getIpcaHistorySidraResponseMetadata(), getIpcaHistorySidraResponseMetadata());
    }

    public static List<IpcaHistorySidraResponse> getIpcaHistorySidraResponseHistoryDataList() {
        List<IpcaHistorySidraResponse> sidraResponseList = new ArrayList<>();
        Random random = new Random();
        int min = 0;
        int max = 10;

        for (int i = 0; i < 10; i++) {
            var ipcaData = new IpcaHistorySidraResponse();
            ipcaData.setD2c("63");
            ipcaData.setD2n("IPCA - Variação mensal");
            ipcaData.setV(String.valueOf(random.nextDouble(max - min) - min));
            min = 1985;
            max = 2024;
            ipcaData.setD3c(String.valueOf(random.nextInt(max - min) - min));
            ipcaData.setD3n("janeiro " + ipcaData.getD3c());
            ipcaData.setD1c("1");
            ipcaData.setD1n("Brasil");
            ipcaData.setNc("1");
            ipcaData.setNn("Brasil");
            ipcaData.setMc("2");

            if(i % 2 == 0) {
                ipcaData.setMn("Número-índice");
            } else {
                ipcaData.setMn("%");
            }

            sidraResponseList.add(ipcaData);
        }

        return sidraResponseList;
    }



    private static IpcaHistorySidraResponse getIpcaHistorySidraResponseMetadata() {
        var sidraResponse = new IpcaHistorySidraResponse();

        sidraResponse.setD2c("Variável (Código)");
        sidraResponse.setD2n("Variável");
        sidraResponse.setV("Valor");
        sidraResponse.setD3c("Mês (Código)");
        sidraResponse.setD3n("Mês");
        sidraResponse.setD1c("Brasil (Código)");
        sidraResponse.setD1n("Brasil");
        sidraResponse.setNc("Nível Territorial (Código)");
        sidraResponse.setNn("Nível Territorial");
        sidraResponse.setMc("Unidade de Medida (Código)");
        sidraResponse.setMn("Unidade de Medida");

        return sidraResponse;
    }

    private static IpcaHistorySidraResponse getIpcaHistorySidraResponseNonHistoryData() {
        var sidraResponse = new IpcaHistorySidraResponse();

        sidraResponse.setD2c("63");
        sidraResponse.setD2n("IPCA - Variação mensal");
        sidraResponse.setV("...");
        sidraResponse.setD3c("197912");
        sidraResponse.setD3n("dezembro 1979");
        sidraResponse.setD1c("1");
        sidraResponse.setD1n("Brasil");
        sidraResponse.setNc("1");
        sidraResponse.setNn("Brasil");
        sidraResponse.setMc("");
        sidraResponse.setMn("");

        return sidraResponse;
    }
    
}
