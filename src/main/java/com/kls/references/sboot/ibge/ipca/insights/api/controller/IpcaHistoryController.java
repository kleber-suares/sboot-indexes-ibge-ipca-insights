package com.kls.references.sboot.ibge.ipca.insights.api.controller;

import com.kls.references.sboot.ibge.ipca.insights.application.service.IpcaDataProcessingService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaGroupedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ibge/ipca")
@Slf4j
public class IpcaHistoryController {

    private final IpcaDataProcessingService ipcaDataProcessingService;

    public IpcaHistoryController(IpcaDataProcessingService ipcaDataProcessingService) {
        this.ipcaDataProcessingService = ipcaDataProcessingService;
    }

    @PostMapping(value = "/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IpcaGroupedData> processIpcaHistoryData() {
        log.info("Request to start processing IPCA history data received.");

        return ResponseEntity.ok(ipcaDataProcessingService.fetchIpcaHistoryData());
    }

}
