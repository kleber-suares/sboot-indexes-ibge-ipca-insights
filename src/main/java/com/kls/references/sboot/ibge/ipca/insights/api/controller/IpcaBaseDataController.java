package com.kls.references.sboot.ibge.ipca.insights.api.controller;

import com.kls.references.sboot.ibge.ipca.insights.application.service.IpcaDataProcessingService;
import com.kls.references.sboot.ibge.ipca.insights.domain.model.IpcaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ibge/ipca")
@Slf4j
public class IpcaBaseDataController {

    private final IpcaDataProcessingService ipcaDataProcessingService;

    public IpcaBaseDataController(IpcaDataProcessingService ipcaDataProcessingService) {
        this.ipcaDataProcessingService = ipcaDataProcessingService;
    }

    @PostMapping(value = "/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IpcaData>> processIpcaHistoryData() {
        log.info("Request to start processing IPCA history data received.");

        return ResponseEntity.ok(ipcaDataProcessingService.fetchIpcaData());
    }

}
