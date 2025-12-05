package com.kls.references.sboot.ibge.ipca.insights.application.service.web;

import com.kls.references.sboot.ibge.ipca.insights.application.mapper.IpcaDataMapper;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpcaDataWebClientService {

    private final IpcaHistoryWebClient ipcaHistoryWebClient;

    public IpcaDataWebClientService(IpcaHistoryWebClient ipcaHistoryWebClient) {
        this.ipcaHistoryWebClient = ipcaHistoryWebClient;
    }

    public List<IpcaData> fetchIpcaData() {
        List<IpcaHistorySidraResponse> response = ipcaHistoryWebClient.getIpcaHistoryData();

        return IpcaDataMapper.toDomain(response);
    }

}
