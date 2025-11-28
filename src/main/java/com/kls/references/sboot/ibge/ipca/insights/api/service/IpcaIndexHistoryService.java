package com.kls.references.sboot.ibge.ipca.insights.api.service;

import com.kls.references.sboot.ibge.ipca.insights.api.infrastructure.IpcaIndexHistoryWebClient;
import org.springframework.stereotype.Service;

@Service
public class IpcaIndexHistoryService {

    private final IpcaIndexHistoryWebClient ipcaIndexHistoryWebClient;

    public IpcaIndexHistoryService(IpcaIndexHistoryWebClient ipcaIndexHistoryWebClient) {
        this.ipcaIndexHistoryWebClient = ipcaIndexHistoryWebClient;
    }

    public String processIpcaHistoryData() {
        return ipcaIndexHistoryWebClient.getIpcaHistoryData();
    }

}
