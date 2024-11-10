package io.fiap.revenda.documentos.driven.client;

import io.fiap.revenda.documentos.driven.client.dto.ImmutableDetranRequestDTO;
import io.fiap.revenda.documentos.driven.exception.BusinessException;
import io.fiap.revenda.documentos.driven.exception.NotFoundException;
import io.fiap.revenda.documentos.driven.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DetranClient {

    private final WebClient client;
    @Value("${detran.webhook}")
    public String webhook;

    public DetranClient(@Qualifier("DetranWebClient") WebClient client) {
        this.client = client;
    }

    public Mono<Void> emitirDetran(String id) {
        return Mono.just(
            ImmutableDetranRequestDTO.builder()
                .id(id)
                .webhook(webhook)
                .build()
        ).flatMap(request ->
            client
                .post()
                .uri("/detran/documentos/emitir")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(status -> HttpStatus.NOT_FOUND == status,
                    clientResponse ->
                        clientResponse.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new NotFoundException(body)))
                )
                .onStatus(HttpStatusCode::is4xxClientError,
                    clientResponse ->
                        clientResponse.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new BusinessException(body)))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                    clientResponse ->
                        clientResponse.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new TechnicalException(body)))
                )
                .toBodilessEntity()
                .then()
        );
    }
}
