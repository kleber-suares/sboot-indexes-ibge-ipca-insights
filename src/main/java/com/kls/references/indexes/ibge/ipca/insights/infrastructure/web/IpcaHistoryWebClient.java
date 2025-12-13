package com.kls.references.indexes.ibge.ipca.insights.infrastructure.web;

import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.web.dto.IpcaHistorySidraResponse;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiTimeoutException;
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
    static final long MAX_RETRY = 3;
    static final long TIMEOUT_SECONDS = 10; //TODO: medir via logs reais de latência para fixar valor mais adequado
    static final long BACKOFF_SECONDS = 2;

    public IpcaHistoryWebClient(
        @Qualifier(IBGE_IPCA_HISTORY_WEB_CLIENT) WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public List<IpcaHistorySidraResponse> fetchIpcaHistory() {

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
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .retryWhen(
                    Retry
                        .backoff(MAX_RETRY, Duration.ofSeconds(BACKOFF_SECONDS))
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
                            String errorMsg = String.format("Request aborted after %d failed retry attempts.", MAX_RETRY);
                            log.error(errorMsg);
                            return new ExternalApiTimeoutException(errorMsg, retrySignal.failure());
                        })
                );

        return validateOptionalResponse(monoResponse.blockOptional());
    }


    private List<IpcaHistorySidraResponse> validateOptionalResponse(
        Optional<List<IpcaHistorySidraResponse>> optionalResponse
    ) {

        return optionalResponse.orElseThrow(
            () -> {
                String errorMsg = "No response returned from the external service.";
                log.error(errorMsg);
                throw new ExternalApiResponseException(errorMsg);
            }
        );
    }

}
