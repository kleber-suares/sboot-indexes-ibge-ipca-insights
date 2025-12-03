package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaHistoryValue;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaInfoValue;

import java.util.List;

public interface IpcaHistoryImport {

    void importHistoryData(List<IpcaHistoryValue> ipcaHistoryValueList);
    void importInfoData(List<IpcaInfoValue> ipcaHistoryValueList);

}
