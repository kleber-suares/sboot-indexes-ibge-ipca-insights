package com.kls.references.indexes.ibge.ipca.insights.application.service.persistence;

import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImportExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IpcaDataRepositoryService {

    private final IpcaDataImportExecutor dataImportExecutor;

    public IpcaDataRepositoryService(
        IpcaDataImportExecutor dataImportExecutor
    ) {
        this.dataImportExecutor = dataImportExecutor;
    }

    public void importIpcaData(List<IpcaData> ipcaDataList, IpcaDataImportLogIds logIds) {

        var partition =
            ipcaDataList
                .stream()
                .collect(Collectors
                    .partitioningBy(IpcaData::isDataHistory)
                );

        var historyList = partition.get(true);
        var infoList = partition.get(false);

        log.info("Submitting async import tasks: {} history records | {} info records", historyList.size(), infoList.size());

        CompletableFuture<Void> historyDataFuture =
            dataImportExecutor.importIpcaHistoryData(historyList, logIds.ipcaHistoryDataImportLogId());
        CompletableFuture<Void> infoDataFuture =
            dataImportExecutor.importIpcaInfoData(infoList, logIds.ipcaInfoDataImportLogId());

        CompletableFuture.allOf(historyDataFuture, infoDataFuture).join();

        log.info("IPCA history data and info data imports completed successfully.");
    }

}
