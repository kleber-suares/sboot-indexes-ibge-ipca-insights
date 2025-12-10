package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.WebClientOperationTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static com.kls.references.indexes.ibge.ipca.insights.infrastructure.config.WebClientConfig.IBGE_IPCA_HISTORY_WEB_CLIENT;

@Component
@Slf4j
public class IpcaHistoryWebClient {

    private final WebClient webClient;

    public IpcaHistoryWebClient(
        @Qualifier(IBGE_IPCA_HISTORY_WEB_CLIENT) WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public Optional<List<IpcaHistorySidraResponse>> fetchIpcaHistory() {
        long maxRetryAttempts = 3;
        long timeoutSeconds = 10; //TODO: medir via logs reais de latência para fixar valor mais adequado
        long backoffSeconds = 2;

        Mono<List<IpcaHistorySidraResponse>> monoResponse =
            webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("formato", "json")
                    .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IpcaHistorySidraResponse>>() {})
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .retryWhen(
                    Retry
                        .backoff(maxRetryAttempts, Duration.ofSeconds(backoffSeconds))
                        .filter(throwable ->
                            throwable instanceof TimeoutException ||
                                throwable instanceof io.netty.handler.timeout.TimeoutException ||
                                throwable instanceof WebClientResponseException.InternalServerError ||
                                (throwable instanceof WebClientResponseException webClientResponseException &&
                                    webClientResponseException.getStatusCode().is5xxServerError())
                        )
                        .doBeforeRetry(retrySignal ->
                            log.warn("Retrying request. Attempt: {}, Failure: {}",
                                retrySignal.totalRetries() + 1,
                                retrySignal.failure().getMessage())
                        )
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            String errorMsg = String.format("Request aborted after %d failed retry attempts.", maxRetryAttempts);
                            log.error(errorMsg);
                            return new WebClientOperationTimeoutException(errorMsg, retrySignal.failure());
                        })
                );

        return monoResponse.blockOptional();
    }

}
