package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImport;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImportImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IpcaDataRepositoryService {

    private final IpcaDataImport repository;

    public IpcaDataRepositoryService(IpcaDataImportImpl repository) {
        this.repository = repository;
    }

    public void importIpcaData(List<IpcaData> ipcaDataList) {

        var partition =
            ipcaDataList
                .stream()
                .collect(Collectors
                    .partitioningBy(IpcaData::isHistory)
                );

        log.info("Calling repository for import of {} items of Ipca History Data.", partition.get(true).size());
        repository.importIpcaHistoryData(partition.get(true));

        log.info("Calling repository for import of {} items of Ipca Info Data.", partition.get(false).size());
        repository.importIpcaInfoData(partition.get(false));
    }

}
