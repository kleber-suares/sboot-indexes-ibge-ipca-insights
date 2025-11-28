package com.kls.references.sboot.ibge.ipca.insights.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ibge.sidra.ipca.table1737.api}")
    private String ipcaTable1737baseUrl;

    @Bean
    public WebClient webClient() {
        return
            WebClient
                .builder()
                .baseUrl(ipcaTable1737baseUrl)
                //TODO: Config para 10MB. Verificar limites e alternativas
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }
}
