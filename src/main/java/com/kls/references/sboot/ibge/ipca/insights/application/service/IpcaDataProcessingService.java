package com.kls.references.sboot.ibge.ipca.insights.application.service;

import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.IpcaDataRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.application.service.web.IpcaDataWebClientService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpcaDataProcessingService {

    private final IpcaDataWebClientService clientService;
    private final IpcaDataRepositoryService repositoryService;

    public IpcaDataProcessingService(IpcaDataWebClientService clientService, IpcaDataRepositoryService repositoryService) {
        this.clientService = clientService;
        this.repositoryService = repositoryService;
    }

    public List<IpcaData> fetchIpcaData() {
        var ipcaData = clientService.fetchIpcaData();

        repositoryService.importIpcaData(ipcaData);

        return ipcaData;
    }

}
