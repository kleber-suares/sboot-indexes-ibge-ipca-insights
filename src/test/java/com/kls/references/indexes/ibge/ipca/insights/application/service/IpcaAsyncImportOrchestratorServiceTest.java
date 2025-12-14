package com.kls.references.indexes.ibge.ipca.insights.application.service;

import com.kls.references.indexes.ibge.ipca.insights.application.service.persistence.ImportLogRepositoryService;
import com.kls.references.indexes.ibge.ipca.insights.application.service.persistence.IpcaDataRepositoryService;
import com.kls.references.indexes.ibge.ipca.insights.application.service.web.IpcaDataFetcherService;
import com.kls.references.indexes.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IpcaAsyncImportOrchestratorServiceTest {

    @Mock
    private ImportLogRepositoryService importLogRepositoryService;

    @Mock
    private IpcaDataFetcherService dataFetcherService;

    @Mock
    private IpcaDataRepositoryService repositoryService;

    @InjectMocks
    private IpcaAsyncImportOrchestratorService orchestratorService;

    private ImportLogEntity historyLog;
    private ImportLogEntity infoLog;

    @BeforeEach
    void setUp() {
        historyLog = mock(ImportLogEntity.class);
        infoLog = mock(ImportLogEntity.class);
    }

    @Test
    void shouldStartAsyncImport_And_ReturnLogIds() {
        when(historyLog.getId()).thenReturn("history-log-id");
        when(infoLog.getId()).thenReturn("info-log-id");

        when(importLogRepositoryService.saveProcessStart(
            OperationType.IPCA_HISTORY_DATA_IMPORT,
            ImportStage.REQUEST_ACKNOWLEDGED
        )).thenReturn(historyLog);

        when(importLogRepositoryService.saveProcessStart(
            OperationType.IPCA_INFO_DATA_IMPORT,
            ImportStage.REQUEST_ACKNOWLEDGED
        )).thenReturn(infoLog);

        IpcaDataImportLogIds result = orchestratorService.startAsyncImport();

        assertThat(result).isNotNull();
        assertThat(result.ipcaHistoryDataImportLogId()).isEqualTo("history-log-id");
        assertThat(result.ipcaInfoDataImportLogId()).isEqualTo("info-log-id");
    }

    @Test
    void shouldExecuteAsyncFlowSuccessfully() {
        when(historyLog.getId()).thenReturn("history-log-id");
        when(infoLog.getId()).thenReturn("info-log-id");

        when(importLogRepositoryService.saveProcessStart(any(), any()))
            .thenReturn(historyLog)
            .thenReturn(infoLog);

        when(importLogRepositoryService.findById("history-log-id"))
            .thenReturn(Optional.of(historyLog));

        when(importLogRepositoryService.findById("info-log-id"))
            .thenReturn(Optional.of(infoLog));

        when(dataFetcherService.fetchIpcaData())
            .thenReturn(List.of(mock(IpcaData.class)));

        orchestratorService.startAsyncImport();

        Awaitility.await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                verify(importLogRepositoryService)
                    .updateProcessDetails("history-log-id", ImportStage.EXTERNAL_SERVICE_FETCH);
                verify(importLogRepositoryService)
                    .updateProcessDetails("info-log-id", ImportStage.EXTERNAL_SERVICE_FETCH);

                verify(repositoryService)
                    .importIpcaData(anyList(), any(IpcaDataImportLogIds.class));

                verify(importLogRepositoryService)
                    .updateProcessEndWithStatusCompleted("history-log-id");
                verify(importLogRepositoryService)
                    .updateProcessEndWithStatusCompleted("info-log-id");
            });
    }

    @Test
    void shouldMarkBothLogsAsFailed_When_ExceptionOccursDuringImport() {
        when(historyLog.getId()).thenReturn("history-log-id");
        when(infoLog.getId()).thenReturn("info-log-id");

        when(importLogRepositoryService.saveProcessStart(any(), any()))
            .thenReturn(historyLog)
            .thenReturn(infoLog);

        when(importLogRepositoryService.findById("history-log-id"))
            .thenReturn(Optional.of(historyLog));

        when(importLogRepositoryService.findById("info-log-id"))
            .thenReturn(Optional.of(infoLog));

        when(dataFetcherService.fetchIpcaData())
            .thenThrow(new RuntimeException("External service failure"));

        orchestratorService.startAsyncImport();

        Awaitility.await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                verify(importLogRepositoryService)
                    .updateProcessEndWithStatusFailed(
                        eq("history-log-id"),
                        contains("failed during the execution")
                    );

                verify(importLogRepositoryService)
                    .updateProcessEndWithStatusFailed(
                        eq("info-log-id"),
                        contains("failed during the execution")
                    );
            });
    }

    @Test
    void shouldReturnImportStatusByJobId() {
        when(importLogRepositoryService.findById("job-id"))
            .thenReturn(Optional.of(historyLog));

        Optional<ImportLogEntity> result =
            orchestratorService.getImportStatus("job-id");

        assertThat(result).isPresent();
        verify(importLogRepositoryService).findById("job-id");
    }
}
