package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SidraIpcaHistoryResponse {

    @JsonProperty("NC")
    private String nc;
    @JsonProperty("NN")
    private String nn;
    @JsonProperty("MC")
    private String mc;
    @JsonProperty("MN")
    private String mn;
    @JsonProperty("V")
    private String v;
    @JsonProperty("D1C")
    private String d1c;
    @JsonProperty("D1N")
    private String d1n;
    @JsonProperty("D2C")
    private String d2c;
    @JsonProperty("D2N")
    private String d2n;
    @JsonProperty("D3C")
    private String d3c;
    @JsonProperty("D3N")
    private String d3n;

}

