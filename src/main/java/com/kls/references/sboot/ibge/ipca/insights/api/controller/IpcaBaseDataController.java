package com.kls.references.sboot.ibge.ipca.insights.api.controller;

import com.kls.references.sboot.ibge.ipca.insights.application.service.IpcaAsyncImportOrchestratorService;
import com.kls.references.sboot.ibge.ipca.insights.infrastructure.persistence.dto.IpcaDataImportLogIds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ibge/ipca")
@Slf4j
public class IpcaBaseDataController {

    private final IpcaAsyncImportOrchestratorService asyncImportService;

    public IpcaBaseDataController(IpcaAsyncImportOrchestratorService asyncImportService) {
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

    @GetMapping(value = "/import/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImportStatus(@PathVariable String jobId) {
        return asyncImportService
            .getImportStatus(jobId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}

