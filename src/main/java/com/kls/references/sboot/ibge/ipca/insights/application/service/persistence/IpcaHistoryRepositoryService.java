package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaGroupedData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaHistoryImport;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaHistoryImportImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IpcaHistoryRepositoryService {

    private final IpcaHistoryImport repository;

    public IpcaHistoryRepositoryService(IpcaHistoryImportImpl repository) {
        this.repository = repository;
    }

    public void importHistoryData(IpcaGroupedData ipcaGroupedData) {
        log.info("Calling repository for import of Ipca History Data.");
        repository.importHistoryData(ipcaGroupedData.getHistoryData());
    }

    public void importInfoData(IpcaGroupedData ipcaGroupedData) {
        log.info("Calling repository for import of Ipca Info Data.");
        repository.importInfoData(ipcaGroupedData.getInfoData());
    }
}
