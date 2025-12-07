package com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository;

import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.exception.ImportOperationException;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.repository.bulk.IpcaHistoryDataBulkOpsImpl;
import com.kls.references.sboot.ibge.ipca.insights.domain.stubs.IpcaDataStub;
import com.mongodb.bulk.BulkWriteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class IpcaDataImportImplTest {

    private IpcaDataImportImpl ipcaImportImpl;
    private IpcaHistoryDataBulkOpsImpl bulkOpsImpl;

    @BeforeEach
    void setUp() {
        bulkOpsImpl = Mockito.mock(IpcaHistoryDataBulkOpsImpl.class);
        ipcaImportImpl = new IpcaDataImportImpl(bulkOpsImpl);
    }

    @Test
    void shouldImportIpcaHistoryDataSuccessfully() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaHistoryDataList = IpcaDataStub.getIpcaHistoryDataList();

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        ipcaImportImpl.importIpcaHistoryData(ipcaHistoryDataList);

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));
    }

    @Test
    void shouldImportIpcaInfoDataSuccessfully() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        ipcaImportImpl.importIpcaInfoData(ipcaInfoDataList);

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));
    }

    @Test
    void shouldThrowException_when_importingIpcaHistoryData_if_IpcaDataObjectIsNotHistoryData() {
        BulkWriteResult bulkWriteResultMock = Mockito.mock(BulkWriteResult.class);
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenReturn(bulkWriteResultMock);

        assertThrows(
            ImportOperationException.class,
            () -> ipcaImportImpl.importIpcaHistoryData(ipcaInfoDataList)
        );

        verify(bulkOpsImpl, times(0)).replaceAll(anyList(), any(Class.class));
    }

    @Test
    void shouldThrowExceptionImport_when_importingIpcaHistoryData_if_anErrorOccursDuringBulkOpsExecution() {
        List<IpcaData> ipcaHistoryDataList = IpcaDataStub.getIpcaHistoryDataList();
        String errorMsg = "Exception Test";

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenThrow(new RuntimeException(errorMsg));

        Exception e =
            assertThrows(
                ImportOperationException.class,
                () -> ipcaImportImpl.importIpcaHistoryData(ipcaHistoryDataList)
            );

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));

        assertThat(e.getCause().getMessage())
            .isNotNull()
            .isNotBlank()
            .isEqualTo(errorMsg);
    }

    @Test
    void shouldThrowExceptionImport_when_importingIpcaInfoData_if_anErrorOccursDuringBulkOpsExecution() {
        List<IpcaData> ipcaInfoDataList = IpcaDataStub.getIpcaInfoDataList();
        String errorMsg = "Exception Test";

        when(bulkOpsImpl.replaceAll(anyList(), any(Class.class)))
            .thenThrow(new RuntimeException(errorMsg));

        Exception e =
            assertThrows(
                ImportOperationException.class,
                () -> ipcaImportImpl.importIpcaInfoData(ipcaInfoDataList)
            );

        verify(bulkOpsImpl, times(1)).replaceAll(anyList(), any(Class.class));

        assertThat(e.getCause().getMessage())
            .isNotNull()
            .isNotBlank()
            .isEqualTo(errorMsg);
    }

}
