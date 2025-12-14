package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.filter;

import com.kls.references.indexes.ibge.ipca.insights.application.constants.ContextKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.MDC;

import java.io.IOException;

import static com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.filter.CorrelationIdFilter.HEADER_CORRELATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void cleanup() {
        MDC.clear();
    }

    @Test
    void shouldGenerateCorrelationId_when_headerIsNull() throws IOException, ServletException {
        try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

            when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn(null);

            filter.doFilter(request, response, filterChain);

            mdcMock.verify(() -> MDC.put(keyCaptor.capture(), valueCaptor.capture()));

            assertThat(keyCaptor.getValue()).isEqualTo(ContextKeys.CORRELATION_ID);
            assertThat(valueCaptor.getValue()).isNotNull().isNotBlank();

            mdcMock.verify(() -> MDC.remove(ContextKeys.CORRELATION_ID));
        }
    }

    @Test
    void shouldGenerateCorrelationId_when_headerIsBlank() throws IOException, ServletException {
        try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

            when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn("");

            filter.doFilter(request, response, filterChain);

            mdcMock.verify(() -> MDC.put(keyCaptor.capture(), valueCaptor.capture()));

            assertThat(keyCaptor.getValue()).isEqualTo(ContextKeys.CORRELATION_ID);
            assertThat(valueCaptor.getValue()).isNotNull().isNotBlank();

            mdcMock.verify(() -> MDC.remove(ContextKeys.CORRELATION_ID));
        }
    }

    @Test
    void shouldUseExistingCorrelationIdFromHeader() throws IOException, ServletException {
        try (MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

            String testCorrelationId = "Test-Correlation-Id";

            when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn(testCorrelationId);

            filter.doFilter(request, response, filterChain);

            mdcMock.verify(() -> MDC.put(keyCaptor.capture(), valueCaptor.capture()));

            assertThat(keyCaptor.getValue()).isEqualTo(ContextKeys.CORRELATION_ID);
            assertThat(valueCaptor.getValue()).isEqualTo(testCorrelationId);

            mdcMock.verify(() -> MDC.remove(ContextKeys.CORRELATION_ID));
        }
    }

    @Test
    void shouldClearMdcAfterFilterExecution() throws IOException, ServletException {
        String testCorrelationId = "Test-Correlation-Id";

        when(request.getHeader(HEADER_CORRELATION_ID)).thenReturn(testCorrelationId);

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, mock(ServletResponse.class), chain);

        assertThat(MDC.get(HEADER_CORRELATION_ID)).isNull();
    }

}
