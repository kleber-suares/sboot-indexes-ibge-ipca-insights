package com.kls.references.sboot.ibge.ipca.insights.application.service;

import com.kls.references.sboot.ibge.ipca.insights.infrastructure.client.IpcaIndexHistoryWebClient;
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
