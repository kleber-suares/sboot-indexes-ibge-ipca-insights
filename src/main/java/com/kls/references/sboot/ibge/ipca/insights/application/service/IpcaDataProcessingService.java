package com.kls.references.sboot.ibge.ipca.insights.application.service;

import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.IpcaHistoryRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.application.service.web.IpcaHistoryWebClientService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaGroupedData;
import org.springframework.stereotype.Service;

@Service
public class IpcaDataProcessingService {

    private final IpcaHistoryWebClientService clientService;
    private final IpcaHistoryRepositoryService repositoryService;

    public IpcaDataProcessingService(IpcaHistoryWebClientService clientService, IpcaHistoryRepositoryService repositoryService) {
        this.clientService = clientService;
        this.repositoryService = repositoryService;
    }

    public IpcaGroupedData fetchIpcaHistoryData() {
        var ipcaGroupedData = clientService.processIpcaHistoryData();

        repositoryService.importHistoryData(ipcaGroupedData);
        repositoryService.importInfoData(ipcaGroupedData);

        return ipcaGroupedData;
    }

}
