package com.kls.references.indexes.ibge.ipca.insights.application.service.persistence;

import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImportExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class IpcaDataRepositoryServiceTest {

    @Mock
    private IpcaDataImportExecutor importRepository;

    @InjectMocks
    private IpcaDataRepositoryService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCallCorrectParallelAsyncMethods() {

        IpcaData historyItem = mock(IpcaData.class);
        when(historyItem.isDataHistory()).thenReturn(true);

        IpcaData infoItem = mock(IpcaData.class);
        when(infoItem.isDataHistory()).thenReturn(false);

        List<IpcaData> inputList = List.of(historyItem, infoItem);

        IpcaDataImportLogIds logIds =
            new IpcaDataImportLogIds("history-log", "info-log");

        CompletableFuture<Void> historyFuture = CompletableFuture.completedFuture(null);
        CompletableFuture<Void> infoFuture = CompletableFuture.completedFuture(null);

        when(importRepository.importIpcaHistoryData(List.of(historyItem), "history-log"))
            .thenReturn(historyFuture);
        when(importRepository.importIpcaInfoData(List.of(infoItem), "info-log"))
            .thenReturn(infoFuture);

        service.importIpcaData(inputList, logIds);

        verify(importRepository)
            .importIpcaHistoryData(List.of(historyItem), "history-log");
        verify(importRepository)
            .importIpcaInfoData(List.of(infoItem), "info-log");

        verifyNoMoreInteractions(importRepository);
    }

}