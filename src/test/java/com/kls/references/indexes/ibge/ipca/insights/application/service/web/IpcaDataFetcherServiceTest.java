package com.kls.references.indexes.ibge.ipca.insights.application.service.web;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import com.kls.references.indexes.ibge.ipca.insights.stubs.IpcaHistorySidraResponseStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IpcaDataFetcherServiceTest {

    private IpcaDataFetcherService service;
    private IpcaHistoryWebClient client;

    @BeforeEach
    void setUp() {
        client = Mockito.mock(IpcaHistoryWebClient.class);
        service = new IpcaDataFetcherService(client);
    }

    @Test
    void shouldMapAndReturnValidListWithTheSameNumberOfItemsReturnedByTheServiceCall() {
        var sidraResponseList = IpcaHistorySidraResponseStub.getAllIpcaHistorySidraResponseList();

        when(client.fetchIpcaHistory())
            .thenReturn(sidraResponseList);

        var ipcaList = service.fetchIpcaData();

        verify(client).fetchIpcaHistory();

        assertThat(ipcaList).hasSameSizeAs(sidraResponseList);
    }

}
