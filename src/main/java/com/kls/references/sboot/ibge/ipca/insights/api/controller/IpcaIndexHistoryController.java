package com.kls.references.sboot.ibge.ipca.insights.api.controller;

import com.kls.references.sboot.ibge.ipca.insights.api.service.IpcaIndexHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ibge/ipca")
@Slf4j
public class IpcaIndexHistoryController {

    private final IpcaIndexHistoryService ipcaIndexHistoryService;

    public IpcaIndexHistoryController(IpcaIndexHistoryService ipcaIndexHistoryService) {
        this.ipcaIndexHistoryService = ipcaIndexHistoryService;
    }

    @PostMapping(value = "/processData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processIpcaHistoryData() {
        try {
            log.info("Request to start processing IPCA history data received.");

            return ResponseEntity.ok(ipcaIndexHistoryService.processIpcaHistoryData());

        } catch (Exception e) {
            String errorMsg = "An error ocurred while processing IPCA history data.";

            log.error(errorMsg, e);

            return ResponseEntity.internalServerError().body(errorMsg);
        }

    }

}
