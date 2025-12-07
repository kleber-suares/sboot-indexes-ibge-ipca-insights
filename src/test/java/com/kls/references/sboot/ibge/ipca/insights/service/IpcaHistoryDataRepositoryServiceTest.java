package com.kls.references.sboot.ibge.ipca.insights.service;

import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.IpcaDataRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImportImpl;
import com.kls.references.sboot.ibge.ipca.insights.domain.stubs.IpcaDataStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IpcaHistoryDataRepositoryServiceTest {

    private IpcaDataImportImpl repository;
    private IpcaDataRepositoryService service;


    @BeforeEach
    void setUp() {
        repository = Mockito.mock(IpcaDataImportImpl.class);
        service = new IpcaDataRepositoryService(repository);
    }

    @Test
    void shouldSubmitAsyncImportTasks() {
        var ipcaDataList = IpcaDataStub.getAllIpcaDataList();

        ArgumentCaptor<List<IpcaData>> historyCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<IpcaData>> infoCaptor = ArgumentCaptor.forClass(List.class);

        when(repository.importIpcaHistoryData(any())).thenReturn(CompletableFuture.completedFuture(null));
        when(repository.importIpcaInfoData(any())).thenReturn(CompletableFuture.completedFuture(null));

        service.importIpcaData(ipcaDataList);

        verify(repository).importIpcaHistoryData(historyCaptor.capture());
        verify(repository).importIpcaInfoData(infoCaptor.capture());

        assertThat(historyCaptor.getValue()).allMatch(IpcaData::isHistory);
        assertThat(infoCaptor.getValue()).allMatch(d -> !d.isHistory());
    }

}
