package com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IpcaDataImportExecutor {

    CompletableFuture<Void> importIpcaHistoryData(List<IpcaData> ipcaHistoryValueList, String logId);
    CompletableFuture<Void> importIpcaInfoData(List<IpcaData> ipcaInfoValueList, String logId);

}
