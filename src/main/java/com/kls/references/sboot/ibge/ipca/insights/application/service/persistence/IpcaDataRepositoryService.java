package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IpcaDataRepositoryService {

    private final IpcaDataImport repository;

    public IpcaDataRepositoryService(IpcaDataImport repository) {
        this.repository = repository;
    }

    public void importIpcaData(List<IpcaData> ipcaDataList) {

        var partition =
            ipcaDataList
                .stream()
                .collect(Collectors
                    .partitioningBy(IpcaData::isHistory)
                );

        var historyList = partition.get(true);
        var infoList = partition.get(false);

        log.info("Submitting async import tasks: {} history records | {} info records", historyList.size(), infoList.size());

        CompletableFuture<Void> historyFuture = repository.importIpcaHistoryData(historyList);
        CompletableFuture<Void> infoFuture = repository.importIpcaInfoData(infoList);

        CompletableFuture.allOf(historyFuture, infoFuture).join();

        log.info("Both imports completed successfully.");
    }

}
