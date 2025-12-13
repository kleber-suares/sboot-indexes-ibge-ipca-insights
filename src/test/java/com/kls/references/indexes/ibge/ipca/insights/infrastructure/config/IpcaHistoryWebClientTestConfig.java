package com.kls.references.indexes.ibge.ipca.insights.infrastructure.config;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.IpcaHistoryWebClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;


@TestConfiguration
@Profile("test")
public class IpcaHistoryWebClientTestConfig {

    public static final String TEST_WEB_CLIENT = "testWebClient";

    @Bean(name = TEST_WEB_CLIENT)
    WebClient ipcaTestWebClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:9999")
            .build();
    }

    @Bean
    IpcaHistoryWebClient ipcaHistoryWebClient(WebClient ipcaTestWebClient) {
        return new IpcaHistoryWebClient(ipcaTestWebClient);
    }

}
