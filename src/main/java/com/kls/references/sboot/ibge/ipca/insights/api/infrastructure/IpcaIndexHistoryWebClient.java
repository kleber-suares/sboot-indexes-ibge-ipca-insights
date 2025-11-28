package com.kls.references.sboot.ibge.ipca.insights.api.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class IpcaIndexHistoryWebClient {

    private final WebClient webClient;

    public IpcaIndexHistoryWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getIpcaHistoryData() {

        Mono<String> monoString =
            webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("formato", "json")
                    .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5));
                //.map(response -> "Response from external API (reactive): " + response);

        log.info("Request executed");

        return monoString.block();
    }

}
