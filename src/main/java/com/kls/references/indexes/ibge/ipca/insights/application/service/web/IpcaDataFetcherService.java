package com.kls.references.indexes.ibge.ipca.insights.application.service.web;

import com.kls.references.indexes.ibge.ipca.insights.application.mapper.IpcaDataMapper;
import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IpcaDataFetcherService {

    private final IpcaHistoryWebClient ipcaHistoryWebClient;

    public IpcaDataFetcherService(IpcaHistoryWebClient ipcaHistoryWebClient) {
        this.ipcaHistoryWebClient = ipcaHistoryWebClient;
    }

    public List<IpcaData> fetchIpcaData() {
        List<IpcaHistorySidraResponse> response =
            ipcaHistoryWebClient.fetchIpcaHistory();

        return IpcaDataMapper.toDomain(response);
    }

}

