package com.kls.references.indexes.ibge.ipca.insights.util;

import com.kls.references.indexes.ibge.ipca.insights.application.constants.ContextKeys;
import org.slf4j.MDC;

import java.util.Map;


public final class CorrelationIdUtil {

    private CorrelationIdUtil() {}

    public static String getCorrelationId() {
        return MDC.get(ContextKeys.CORRELATION_ID);
    }

    public static void setCorrelationId(String correlationId) {
        MDC.put(ContextKeys.CORRELATION_ID, correlationId);
    }

    public static void clearCorrelationId() {
        MDC.remove(ContextKeys.CORRELATION_ID);
    }

    public static void setContextMap(Map<String, String> contextMap) {
        for(Map.Entry<String, String> entry : contextMap.entrySet()) {
            MDC.put(entry.getKey(), entry.getValue());
        }
    }

    public static void clearContextMap(Map<String, String> contextMap) {
        for(Map.Entry<String, String> entry : contextMap.entrySet()) {
            MDC.remove(entry.getKey());
        }
    }

}
