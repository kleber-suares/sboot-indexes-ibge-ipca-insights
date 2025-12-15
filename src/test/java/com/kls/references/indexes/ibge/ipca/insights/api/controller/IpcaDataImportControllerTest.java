package com.kls.references.indexes.ibge.ipca.insights.api.controller;

import com.kls.references.indexes.ibge.ipca.insights.application.service.IpcaAsyncImportOrchestratorService;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStage;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.ImportStatus;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.enums.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IpcaDataImportController.class)
class IpcaBaseDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IpcaAsyncImportOrchestratorService asyncImportService;

    @Test
    void shouldStartAsyncImport_And_ReturnAcceptedWithLogIds() throws Exception {
        IpcaDataImportLogIds logIds =
            new IpcaDataImportLogIds("history-id", "info-id");

        when(asyncImportService.startAsyncImport()).thenReturn(logIds);

        mockMvc.perform(post("/ibge/ipca/fetch"))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.ipcaHistoryDataImportLogId").value("history-id"))
            .andExpect(jsonPath("$.ipcaInfoDataImportLogId").value("info-id"));
    }

    @Test
    void shouldReturnImportStatus_When_JobExists() throws Exception {
        ImportLogEntity importLog = new ImportLogEntity(
            OperationType.IPCA_HISTORY_DATA_IMPORT,
            ImportStatus.COMPLETED,
            ImportStage.EXTERNAL_SERVICE_FETCH,
            "IBGE-SIDRA API",
            "Web"
        );

        importLog.setEndDateTime(LocalDateTime.now());

        when(asyncImportService.getImportStatus("job-id-123"))
            .thenReturn(Optional.of(importLog));

        mockMvc.perform(get("/ibge/ipca/imports/job-id-123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.operationType").value("IPCA_HISTORY_DATA_IMPORT"))
            .andExpect(jsonPath("$.stage").exists())
            .andExpect(jsonPath("$.endDateTime").exists())
            .andExpect(jsonPath("$.durationText").exists())
            .andExpect(jsonPath("$.durationMillis").exists());
    }

    @Test
    void shouldReturnNotFound_When_JobDoesNotExist() throws Exception {
        when(asyncImportService.getImportStatus("invalid-id"))
            .thenReturn(Optional.empty());

        mockMvc.perform(get("/ibge/ipca/imports/invalid-id"))
            .andExpect(status().isNotFound());
    }


}
