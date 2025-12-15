package com.kls.references.indexes.ibge.ipca.insights.application.service.persistence;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportResults;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStatus;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.repository.ImportLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ImportLogRepositoryServiceTest {

    @Mock
    private ImportLogRepository importLogRepository;

    @InjectMocks
    private ImportLogRepositoryService service;

    private ImportLogEntity importLogStub;

    @BeforeEach
    void setUp() {
        importLogStub = new ImportLogEntity(
            OperationType.IPCA_HISTORY_DATA_IMPORT,
            ImportStatus.STARTED,
            ImportStage.REQUEST_ACKNOWLEDGED,
            "IBGE-SIDRA API",
            "Web"
        );
    }


    @Test
    void shouldReturnImportLog_When_FindByIdExists() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        Optional<ImportLogEntity> result = service.findById("logId-123");

        assertThat(result).isPresent();
        assertThat(result.get().getOperationType()).isEqualTo(OperationType.IPCA_HISTORY_DATA_IMPORT);
        assertThat(result.get().getStatus()).isEqualTo(ImportStatus.STARTED);
        assertThat(result.get().getStage()).isEqualTo(ImportStage.REQUEST_ACKNOWLEDGED);
        assertThat(result.get().getSourceDataName()).isEqualTo("IBGE-SIDRA API");
        assertThat(result.get().getSourceDataLocation()).isEqualTo("Web");
    }


    @Test
    void shouldSaveAndReturnImportLog_When_ProcessStarts() {
        when(importLogRepository.save(any(ImportLogEntity.class)))
            .thenAnswer(invocation ->
                invocation.getArgument(0)
            );

        ImportLogEntity saved =
            service.saveProcessStart(
                OperationType.IPCA_HISTORY_DATA_IMPORT,
                ImportStage.REQUEST_ACKNOWLEDGED
            );

        assertThat(saved.getStatus()).isEqualTo(ImportStatus.STARTED);
        assertThat(saved.getStage()).isEqualTo(ImportStage.REQUEST_ACKNOWLEDGED);

        verify(importLogRepository, times(2)).save(any(ImportLogEntity.class));
    }


    @Test
    void shouldUpdateStageAndEndDate_When_UpdateProcessDetails() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        service.updateProcessDetails("logId-123", ImportStage.EXTERNAL_SERVICE_FETCH);

        ArgumentCaptor<ImportLogEntity> captor =
            ArgumentCaptor.forClass(ImportLogEntity.class);

        verify(importLogRepository).save(captor.capture());

        ImportLogEntity updated = captor.getValue();

        assertThat(updated.getStage()).isEqualTo(ImportStage.EXTERNAL_SERVICE_FETCH);
        assertThat(updated.getEndDateTime()).isNotNull();
    }


    @Test
    void shouldMarkProcessAsCompleted_When_NoImportResultsProvided() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        service.updateProcessEndWithStatusCompleted("logId-123");

        ArgumentCaptor<ImportLogEntity> captor =
            ArgumentCaptor.forClass(ImportLogEntity.class);

        verify(importLogRepository).save(captor.capture());

        ImportLogEntity updated = captor.getValue();

        assertThat(updated.getStatus()).isEqualTo(ImportStatus.COMPLETED);
        assertThat(updated.getEndDateTime()).isNotNull();
    }


    @Test
    void shouldMarkProcessAsCompletedAndSetCounters_When_ImportResultsProvided() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        IpcaDataImportResults results =
            new IpcaDataImportResults(5, 10, 2);

        service.updateProcessEndWithStatusCompleted("logId-123", results);

        ArgumentCaptor<ImportLogEntity> captor =
            ArgumentCaptor.forClass(ImportLogEntity.class);

        verify(importLogRepository).save(captor.capture());

        ImportLogEntity updated = captor.getValue();

        assertThat(updated.getStatus()).isEqualTo(ImportStatus.COMPLETED);
        assertThat(updated.getDeletedCount()).isEqualTo(5);
        assertThat(updated.getInsertedCount()).isEqualTo(10);
        assertThat(updated.getUpdatedCount()).isEqualTo(2);
        assertThat(updated.getEndDateTime()).isNotNull();
    }


    @Test
    void shouldHandleNullImportResultsSafely() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        service.updateProcessEndWithStatusCompleted("logId-123", null);

        ArgumentCaptor<ImportLogEntity> captor =
            ArgumentCaptor.forClass(ImportLogEntity.class);

        verify(importLogRepository).save(captor.capture());

        ImportLogEntity updated = captor.getValue();

        assertThat(updated.getStatus()).isEqualTo(ImportStatus.COMPLETED);
        assertThat(updated.getDeletedCount()).isNull();
        assertThat(updated.getInsertedCount()).isNull();
        assertThat(updated.getUpdatedCount()).isNull();
    }


    @Test
    void shouldMarkProcessAsFailed_When_ErrorOccurs() {
        when(importLogRepository.findById("logId-123"))
            .thenReturn(Optional.of(importLogStub));

        service.updateProcessEndWithStatusFailed("logId-123", "An error occurred");

        ArgumentCaptor<ImportLogEntity> captor =
            ArgumentCaptor.forClass(ImportLogEntity.class);

        verify(importLogRepository).save(captor.capture());

        ImportLogEntity updated = captor.getValue();

        assertThat(updated.getStatus()).isEqualTo(ImportStatus.FAILED);
        assertThat(updated.getErrorMessage()).isEqualTo("An error occurred");
        assertThat(updated.getEndDateTime()).isNotNull();
    }
    
    
}


