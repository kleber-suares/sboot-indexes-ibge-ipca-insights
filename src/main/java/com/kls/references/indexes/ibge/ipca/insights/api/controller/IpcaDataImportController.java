package com.kls.references.indexes.ibge.ipca.insights.api.controller;

import com.kls.references.indexes.ibge.ipca.insights.api.response.ImportLogResponse;
import com.kls.references.indexes.ibge.ipca.insights.application.service.IpcaAsyncImportOrchestratorService;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.persistence.entity.ImportLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ibge/ipca")
@Slf4j
public class IpcaDataImportController {

    private final IpcaAsyncImportOrchestratorService asyncImportService;

    public IpcaDataImportController(IpcaAsyncImportOrchestratorService asyncImportService) {
        this.asyncImportService = asyncImportService;
    }


    @PostMapping(value = "/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IpcaDataImportLogIds> startAsyncImport() {
        log.info("Request to start processing IPCA data received.");

        IpcaDataImportLogIds logIds = asyncImportService.startAsyncImport();

        return ResponseEntity
            .accepted()
            .body(logIds);
    }


    @GetMapping(value = "/imports/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImportStatus(@PathVariable String jobId) {

        return asyncImportService
            .getImportStatus(jobId)
            .map(importLog ->
                ResponseEntity
                    .ok()
                    .body(mapImportLog(importLog))
            )
            .orElse(
                ResponseEntity
                    .notFound()
                    .build()
            );

    }


    private ImportLogResponse mapImportLog(ImportLogEntity importLog) {

        return new ImportLogResponse(
            importLog.getId(),
            importLog.getStartDateTime(),
            importLog.getEndDateTime(),
            importLog.getOperationType(),
            importLog.getStatus(),
            importLog.getStage(),
            importLog.getDurationText(),
            importLog.getDurationMillis(),
            importLog.getSourceDataName(),
            importLog.getSourceDataLocation(),
            importLog.getErrorMessage(),
            importLog.getDeletedCount(),
            importLog.getInsertedCount(),
            importLog.getUpdatedCount()
        );

    }

}

