package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiTimeoutException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IpcaHistoryWebClientWithMockWebServerTest{

    private static MockWebServer mockWebServer;
    private IpcaHistoryWebClient ipcaHistoryWebClient;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient client = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build();

        ipcaHistoryWebClient = new IpcaHistoryWebClient(client);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnIpcaHistorySuccessfully() {
        String json = """
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
                """;

        MockResponse mockResponse = new MockResponse()
            .setBody(json)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterSecondRetryFollowingError500() {
        String json = """
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
                """;

        MockResponse mockResponse = new MockResponse()
            .setBody(json)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(mockResponse);

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(mockWebServer.getRequestCount()).isEqualTo(2);
        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterThirdRetryFollowingTwoTimeoutExceptions() {
        long delay = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS) + 1;

        String json = """
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
                """;

        MockResponse mockResponse = new MockResponse()
            .setBody(json)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(new MockResponse().setHeadersDelay(delay, TimeUnit.SECONDS));
        mockWebServer.enqueue(new MockResponse().setHeadersDelay(delay, TimeUnit.SECONDS));
        mockWebServer.enqueue(mockResponse);

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterThirdRetryFollowingError500AndTimeoutException() {
        long delay = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS) + 1;

        String json = """
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
                """;

        MockResponse mockResponse = new MockResponse()
            .setBody(json)
            .addHeader("Content-Type", "application/json")
            .setResponseCode(200);

        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setHeadersDelay(delay, TimeUnit.SECONDS));
        mockWebServer.enqueue(mockResponse);

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldRetryAndThenThrowTimeoutException_When_AllAttemptsTimeout() {
        long delay = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS) + 1;

        for (long i = 1; i <= IpcaHistoryWebClient.MAX_RETRY ; i++) {
            mockWebServer.enqueue(new MockResponse().setHeadersDelay(delay, TimeUnit.SECONDS));
        }

        var e = assertThrows(
            ExternalApiTimeoutException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );

        assertThat(e.getCause()).isInstanceOf(java.util.concurrent.TimeoutException.class);
    }


    @Test
    void shouldRetryAndThenThrowTimeoutException_When_AllAttemptsFailOnError5xxx() {

        for (long i = 1; i <= IpcaHistoryWebClient.MAX_RETRY ; i++) {
            mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        }

        var e = assertThrows(
            ExternalApiTimeoutException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );

        /*
            Ao contrario do WireMock, um erro 500 gerado com MockWebServer acaba sendo dominado pelo mecanismo
            de timeout/retry do WebClient e o TimeoutException vira a causa raiz.
         */
        assertThat(e.getCause()).isInstanceOf(java.util.concurrent.TimeoutException.class);
    }


    @Test
    void shouldThrowExternalApiResponseException_When_ResponseIsEmpty() {
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setBody("") // empty body => Optional.empty
        );

        assertThrows(
            ExternalApiResponseException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );
    }

}

