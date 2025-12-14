package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.config.IpcaHistoryWebClientTestConfig;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiTimeoutException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.kls.references.indexes.ibge.ipca.insights.infrastructure.config.IpcaHistoryWebClientTestConfig.TEST_WEB_CLIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Import(IpcaHistoryWebClientTestConfig.class)
@ActiveProfiles("test")
class IpcaHistoryWebClientWithWireMockTest {

    private IpcaHistoryWebClient ipcaHistoryWebClient;

    IpcaHistoryWebClientWithWireMockTest(
        @Qualifier(TEST_WEB_CLIENT) WebClient webClient
    ) {
        ipcaHistoryWebClient = new IpcaHistoryWebClient(webClient);
    }

    static WireMockServer wireMockServer;

    @BeforeAll
    static void startServer() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
        configureFor("localhost", 9999);
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    @Test
    void shouldReturnIpcaHistorySuccessfully() {

        stubFor(get(urlPathEqualTo("/"))
            .withQueryParam("formato", equalTo("json"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        [
                          {"D1C":"202201","V":0.54},
                          {"D1C":"202202","V":1.01}
                        ]
                    """)
            )
        );

        var result = ipcaHistoryWebClient.fetchIpcaHistory();

        assertEquals(2, result.size());
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterSecondRetryFollowingError500() {
        String json = """
            [
              {"D1C":"202201","V":0.54},
              {"D1C":"202202","V":1.01}
            ]
        """;

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("Retry Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(serverError()) // primeira tentativa: 500
            .willSetStateTo("SecondTry")
        );

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("Retry Scenario")
            .whenScenarioStateIs("SecondTry")
            .willReturn(okJson(json)) // segunda tentativa: OK
        );

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterThirdRetryFollowingTwoTimeoutExceptions() {
        long delayMs = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS + 1) * 1000;

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("Timeout Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(ok()
                .withFixedDelay((int) delayMs)) // primeira tentativa: timeout
            .willSetStateTo("SecondTry")
        );

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("Timeout Scenario")
            .whenScenarioStateIs("SecondTry")
            .willReturn(ok()
                .withFixedDelay((int) delayMs)) // segunda tentativa: timeout
            .willSetStateTo("ThirdTry")
        );

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("Timeout Scenario")
            .whenScenarioStateIs("ThirdTry") // terceira tentativa: ok
            .willReturn(okJson("""
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
            """))
        );

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldReturnValidList_When_ApiSucceedsAfterThirdRetryFollowingError500AndTimeoutException() {
        long delayMs = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS + 1) * 1000;

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("ServerError and Retry Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(serverError()) // primeira tentativa: 500
            .willSetStateTo("SecondTry")
        );

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("ServerError and Retry Scenario")
            .whenScenarioStateIs("SecondTry")
            .willReturn(ok()
                .withFixedDelay((int) delayMs)) // segunda tentativa: timeout
            .willSetStateTo("ThirdTry")
        );

        stubFor(get(urlPathEqualTo("/"))
            .inScenario("ServerError and Retry Scenario")
            .whenScenarioStateIs("ThirdTry") // terceira tentativa: ok
            .willReturn(okJson("""
                [
                  {"D1C":"202201","V":0.54},
                  {"D1C":"202202","V":1.01}
                ]
            """))
        );

        List<IpcaHistorySidraResponse> resultList = ipcaHistoryWebClient.fetchIpcaHistory();

        assertThat(resultList).hasSize(2);
    }


    @Test
    void shouldRetryAndThenThrowTimeoutException_When_AllAttemptsTimeout() {
        long delayMs = (IpcaHistoryWebClient.TIMEOUT_SECONDS + IpcaHistoryWebClient.BACKOFF_SECONDS + 1) * 1000;

        stubFor(get(urlPathEqualTo("/"))
            .willReturn(ok().withFixedDelay((int) delayMs))
        );

        var e = assertThrows(
            ExternalApiTimeoutException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );

        assertThat(e.getCause()).isInstanceOf(java.util.concurrent.TimeoutException.class);
    }


    @Test
    void shouldRetryAndThenThrowTimeoutException_When_AllAttemptsFailOnError5xxx() {
        stubFor(get(urlPathEqualTo("/"))
            .willReturn(serverError())
        );

        var e = assertThrows(
            ExternalApiTimeoutException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );

        /*
            Ao contrario do MockWebServer, o WireMock simula um servidor HTTP real
            e o WebClient consegue interpretar corretamente o status HTTP 500
            e criar um WebClientResponseException.InternalServerError
         */
        assertThat(e.getCause()).isInstanceOf(WebClientResponseException.InternalServerError.class);
    }


    @Test
    void shouldThrowExternalApiResponseException_When_ResponseIsEmpty() {
        stubFor(get(urlPathEqualTo("/"))
            .withQueryParam("formato", equalTo("json"))
            .willReturn(ok())
        );

        assertThrows(
            ExternalApiResponseException.class,
            () -> ipcaHistoryWebClient.fetchIpcaHistory()
        );
    }

}
