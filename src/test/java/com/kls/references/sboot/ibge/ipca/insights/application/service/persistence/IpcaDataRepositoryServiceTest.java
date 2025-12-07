package com.kls.references.sboot.ibge.ipca.insights.application.service.persistence;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.IpcaDataImport;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class IpcaDataRepositoryServiceTest {

    @Test
    void shouldExecuteRepositoryCallsInParallel() {
        IpcaDataImport repository = mock(IpcaDataImport.class);

        when(repository.importIpcaHistoryData(any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        when(repository.importIpcaInfoData(any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        IpcaDataRepositoryService service = new IpcaDataRepositoryService(repository);

        IpcaData h = mock(IpcaData.class);
        IpcaData i = mock(IpcaData.class);

        when(h.isHistory()).thenReturn(true);
        when(i.isHistory()).thenReturn(false);

        service.importIpcaData(List.of(h, i));

        verify(repository, timeout(1500)).importIpcaHistoryData(any());
        verify(repository, timeout(1500)).importIpcaInfoData(any());
    }
}