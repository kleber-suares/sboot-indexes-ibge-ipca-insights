package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;

import java.util.List;

public interface IpcaDataImport {

    void importIpcaHistoryData(List<IpcaData> ipcaHistoryValueList);
    void importIpcaInfoData(List<IpcaData> ipcaInfoValueList);

}
