package com.kls.references.sboot.ibge.ipca.insights.application.service;

import com.kls.references.sboot.ibge.ipca.insights.infrastructure.dto.IpcaHistoryExternalResponse;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.client.IpcaHistoryWebClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpcaHistoryService {

    private final IpcaHistoryWebClient ipcaHistoryWebClient;

    public IpcaHistoryService(IpcaHistoryWebClient ipcaHistoryWebClient) {
        this.ipcaHistoryWebClient = ipcaHistoryWebClient;
    }

    public List<IpcaHistoryExternalResponse> processIpcaHistoryData() {
        return ipcaHistoryWebClient.getIpcaHistoryData();
    }

}
