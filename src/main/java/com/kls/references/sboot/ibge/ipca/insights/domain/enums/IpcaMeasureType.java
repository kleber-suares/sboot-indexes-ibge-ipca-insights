package com.kls.references.sboot.ibge.ipca.insights.domain.enums;

import java.util.Arrays;

public enum IpcaMeasureType {
    HISTORY_NUMERO_INDICE("30", "Número-índice"),
    HISTORY_PERCENTUAL("2", "%");

    private final String code;
    private final String label;

    IpcaMeasureType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static boolean isHistory(String code, String label) {
        return Arrays.stream(values())
            .anyMatch(t ->
                t.code.equals(code) &&
                t.label.equalsIgnoreCase(label)
            );
    }

}
