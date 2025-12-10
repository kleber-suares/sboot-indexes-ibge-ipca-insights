package com.kls.references.indexes.ibge.ipca.insights.application.service.web;

import com.kls.references.indexes.ibge.ipca.insights.application.mapper.IpcaDataMapper;
import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IpcaDataFetcherService {

    private final IpcaHistoryWebClient ipcaHistoryWebClient;

    public IpcaDataFetcherService(IpcaHistoryWebClient ipcaHistoryWebClient) {
        this.ipcaHistoryWebClient = ipcaHistoryWebClient;
    }

    public List<IpcaData> fetchIpcaData() {
        Optional<List<IpcaHistorySidraResponse>> optionalResponse =
            ipcaHistoryWebClient.fetchIpcaHistory();

        List<IpcaHistorySidraResponse> response =
            optionalResponse.orElseThrow(
                () -> {
                    String errorMsg = "No data received from external service";
                    log.error(errorMsg);
                    throw new ExternalApiResponseException(errorMsg);
                }
            );

        return IpcaDataMapper.toDomain(response);
    }

}
