package com.kls.references.sboot.ibge.ipca.insights.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

class MdcTaskDecoratorMockedTest {

    private final TaskDecorator decorator = new MdcTaskDecorator();

    @Test
    void shouldApplyAndRestoreMdcContext() {

        Map<String, String> capturedContext = new HashMap<>();
        capturedContext.put("X-Correlation-Id", "abc123");

        Map<String, String> previousContext = new HashMap<>();
        previousContext.put("user", "john");

        AtomicBoolean runnableExecuted = new AtomicBoolean(false);

        try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {

            // FIRST call: when capturing
            mdcMock.when(MDC::getCopyOfContextMap).thenReturn(capturedContext);

            Runnable original = () -> runnableExecuted.set(true);

            Runnable decorated = decorator.decorate(original);

            // SECOND call: when reading "previous" inside decorated
            mdcMock.when(MDC::getCopyOfContextMap).thenReturn(previousContext);

            decorated.run();

            // validate calls
            mdcMock.verify(() -> MDC.setContextMap(capturedContext), times(1));
            mdcMock.verify(() -> MDC.setContextMap(previousContext), times(1));
            mdcMock.verify(MDC::clear, times(0)); // never called because previous != null

            assertThat(runnableExecuted).isTrue();
        }
    }

    @Test
    void shouldClearMdc_If_PreviousContextIsNull() {

        Map<String, String> previousMapCaptured = new HashMap<>();
        previousMapCaptured.put("previousKey", "def456");

        AtomicBoolean executed = new AtomicBoolean(false);

        try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {

            // FIRST call: capturing outer MDC
            mdcMock.when(MDC::getCopyOfContextMap).thenReturn(previousMapCaptured);

            Runnable original = () -> executed.set(true);
            Runnable decorated = decorator.decorate(original);

            // SECOND call: reading "previous" context
            mdcMock.when(MDC::getCopyOfContextMap).thenReturn(null);

            decorated.run();

            mdcMock.verify(() -> MDC.setContextMap(previousMapCaptured), times(1));
            mdcMock.verify(MDC::clear, times(1));  // restore previous=null → clear()

            assertThat(executed).isTrue();
        }
    }

}