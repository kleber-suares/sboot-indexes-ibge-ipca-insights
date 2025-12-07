package com.kls.references.sboot.ibge.ipca.insights.infrastructure.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import static org.assertj.core.api.Assertions.assertThat;

class MdcTaskDecoratorRealTest {

    private final TaskDecorator decorator = new MdcTaskDecorator();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldApplyCapturedMdcInsideAsyncExecution() {

        MDC.put("X-Correlation-Id", "abc123");

        Runnable original = () -> {
            assertThat(MDC.get("X-Correlation-Id")).isEqualTo("abc123");
        };

        decorator.decorate(original).run();
    }

    @Test
    void shouldRestorePreviousMdcAfterExecution() {

        MDC.put("outerKey", "A");

        Runnable original = () -> MDC.put("innerKey", "B");

        decorator.decorate(original).run();

        // inner key must NOT leak
        assertThat(MDC.get("innerKey")).isNull();

        // outer key must remain
        assertThat(MDC.get("outerKey")).isEqualTo("A");
    }

    @Test
    void shouldClearMdc_if_OuterIsEmpty() {

        Runnable original = () -> MDC.put("somethingKey", "somethingValue");

        decorator.decorate(original).run();

        // After decorated runnable completes, MDC must be empty
        assertThat(MDC.getCopyOfContextMap()).isNull();
    }
}