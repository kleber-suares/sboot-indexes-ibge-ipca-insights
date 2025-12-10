package com.kls.references.indexes.ibge.ipca.insights.api.controller;


import com.kls.references.indexes.ibge.ipca.insights.api.request.CumulativeInflationRateRequest;
import com.kls.references.indexes.ibge.ipca.insights.api.response.CumulativeInflationRateResponse;
import com.kls.references.indexes.ibge.ipca.insights.application.service.persistence.IpcaCalcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ibge/ipca")
public class IpcaCalculationController {

    private final IpcaCalcService service;

    public IpcaCalculationController(IpcaCalcService service) {
        this.service = service;
    }

    @PostMapping("/taxa-acumulada")
    public ResponseEntity<CumulativeInflationRateResponse> calcular(@RequestBody CumulativeInflationRateRequest request) {

        return ResponseEntity.ok(
            service.calculateCumulativeInflation(
                request.startYear(),
                request.startMonth(),
                request.endYear(),
                request.endMonth()
            )
        );
    }
}