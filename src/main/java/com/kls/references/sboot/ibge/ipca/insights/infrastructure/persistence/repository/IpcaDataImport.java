package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IpcaDataImport {

    CompletableFuture<Void> importIpcaHistoryData(List<IpcaData> ipcaHistoryValueList);
    CompletableFuture<Void> importIpcaInfoData(List<IpcaData> ipcaInfoValueList);

}
