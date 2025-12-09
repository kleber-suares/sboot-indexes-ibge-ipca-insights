package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.application.service.persistence.ImportLogRepositoryService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.bulk.IpcaHistoryDataBulkOpsImpl;
import com.kls.references.sboot.ibge.ipca.insights.domain.stubs.IpcaDataStub;
import com.mongodb.bulk.BulkWriteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class IpcaDataImportExecutorImplTest {

    private IpcaHistoryDataBulkOpsImpl bulkOpsImpl;
    private IpcaDataImportExecutorImpl ipcaImportImpl;
    private ImportLogRepositoryService logRepositoryService;


    @BeforeEach
    void setUp() {
        bulkOpsImpl = Mockito.mock(IpcaHistoryDataBulkOpsImpl.class);
        logRepositoryService = Mockito.mock(ImportLogRepositoryService.class);
        ipcaImportImpl = new IpcaDataImportExecutorImpl(bulkOpsImpl, logRepositoryService);
    }

    @Test
    void shouldImportIpcaHistoryDataSuccessfully() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaHistoryDataList = IpcaDataStub.getIpcaHistoryDataList();
        String logId = "abc-123";

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        ipcaImportImpl.importIpcaHistoryData(ipcaHistoryDataList, logId);

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));
    }

    @Test
    void shouldImportIpcaInfoDataSuccessfully() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();
        String logId = "abc-123";

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        ipcaImportImpl.importIpcaInfoData(ipcaInfoDataList, logId);

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));
    }

    @Test
    void shouldThrowException_when_importingIpcaHistoryData_if_IpcaDataObjectIsNotHistoryData() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();
        String logId = "abc-123";

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        assertThrows(
            Exception.class,
            () -> ipcaImportImpl.importIpcaHistoryData(ipcaInfoDataList, logId)
        );

        verify(bulkOpsImpl, times(0)).replaceAll(anyList(), any(Class.class));
        verify(logRepositoryService).updateProcessEndWithStatusFailed(eq(logId), anyString());
    }

    @Test
    void shouldThrowExceptionImport_when_importingIpcaHistoryData_if_anErrorOccursDuringBulkOpsExecution() {
        List<IpcaData> ipcaHistoryDataList = IpcaDataStub.getIpcaHistoryDataList();
        String errorMsg = "Exception Test";
        String logId = "abc-123";
        RuntimeException expectedException = new RuntimeException(errorMsg);

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenThrow(new RuntimeException(errorMsg));

        Exception e =
            assertThrows(
                expectedException.getClass(),
                () -> ipcaImportImpl.importIpcaHistoryData(ipcaHistoryDataList, logId)
            );

        verify(bulkOpsImpl).replaceAll(anyList(), any(Class.class));
        verify(logRepositoryService).updateProcessEndWithStatusFailed(eq(logId), anyString());
    }

    @Test
    void shouldThrowExceptionImport_when_importingIpcaInfoData_if_anErrorOccursDuringBulkOpsExecution() {
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();
        String errorMsg = "Exception Test";
        String logId = "abc-123";
        RuntimeException expectedException = new RuntimeException(errorMsg);

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenThrow(expectedException);

        Exception e =
            assertThrows(
                expectedException.getClass(),
                () -> ipcaImportImpl.importIpcaInfoData(ipcaInfoDataList, logId)
            );

        verify(bulkOpsImpl).replaceAll(anyList(), any(Class.class));
        verify(logRepositoryService).updateProcessEndWithStatusFailed(eq(logId), anyString());
    }

}
