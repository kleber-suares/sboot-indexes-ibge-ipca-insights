package com.kls.references.sboot.ibge.ipca.insights.application.service.web;

import com.kls.references.sboot.ibge.ipca.insights.application.mapper.IpcaGroupedDataMapper;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaGroupedData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpcaHistoryWebClientService {

    private final IpcaHistoryWebClient ipcaHistoryWebClient;

    public IpcaHistoryWebClientService(IpcaHistoryWebClient ipcaHistoryWebClient) {
        this.ipcaHistoryWebClient = ipcaHistoryWebClient;
    }

    public IpcaGroupedData processIpcaHistoryData() {
        List<IpcaHistorySidraResponse> response = ipcaHistoryWebClient.getIpcaHistoryData();

        return IpcaGroupedDataMapper.toDomain(response);
    }

}
