package com.kls.references.sboot.ibge.ipca.insights.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class IpcaGroupedData {
    private List<IpcaInfoValue> infoData;
    private List<IpcaHistoryValue> historyData;
}
