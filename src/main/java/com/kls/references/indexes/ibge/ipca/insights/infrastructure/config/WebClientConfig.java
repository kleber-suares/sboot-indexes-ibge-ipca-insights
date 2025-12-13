package com.kls.references.indexes.ibge.ipca.insights.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
@Profile("!test")
public class WebClientConfig {

    @Value("${ibge.sidra.ipca.table1737.api}")
    private String ipcaTable1737BaseUrl;

    public static final String IBGE_IPCA_HISTORY_WEB_CLIENT = "ibgeIpcaHistoryDataWebClient";

    @Bean(name = IBGE_IPCA_HISTORY_WEB_CLIENT)
    public WebClient webClient() {
        return
            WebClient
                .builder()
                .baseUrl(ipcaTable1737BaseUrl)
                //TODO: Config para 10MB. Verificar limites e alternativas
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .filter(logRequestUri())
                .build();
    }

    private ExchangeFilterFunction logRequestUri() {
        return (clientRequest, next) -> {
            log.info("Sending request to {}", clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

}
