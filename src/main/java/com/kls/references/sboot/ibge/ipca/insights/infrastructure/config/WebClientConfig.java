package com.kls.references.sboot.ibge.ipca.insights.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
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
//                .clientConnector(new ReactorClientHttpConnector(getHttpClientConnector())) //Nao necessario por enquanto. Ja existe timeout na classe implementadora
                .build();
    }

//    private HttpClient getHttpClientConnector() {
//        return HttpClient.create()
//            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000) //5000 Connection timeout
//            .responseTimeout(Duration.ofSeconds(1)) //3 ou 5 Response timeout
//            .doOnConnected(
//                conn ->
//                    conn
//                        .addHandlerLast(new ReadTimeoutHandler(1)) //3 if  the server is not sending any data, even if the connection itself is still open.
//                        .addHandlerLast(new WriteTimeoutHandler(1)) //3 if client is unable to send data to the server
//            );
//    }

    private ExchangeFilterFunction logRequestUri() {
        return (clientRequest, next) -> {
            log.info("Sending request to {}", clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

}
