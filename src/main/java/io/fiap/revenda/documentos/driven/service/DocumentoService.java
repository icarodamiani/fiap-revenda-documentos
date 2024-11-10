package io.fiap.revenda.documentos.driven.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fiap.revenda.documentos.driven.client.DetranClient;
import io.fiap.revenda.documentos.driven.client.dto.EmitirDocumentoRequestDTO;
import io.fiap.revenda.documentos.driven.client.dto.ImmutableMarcarVeiculoVendidoRequestDTO;
import io.fiap.revenda.documentos.driven.domain.Documento;
import io.fiap.revenda.documentos.driven.domain.ImmutableDocumento;
import io.fiap.revenda.documentos.driven.exception.BusinessException;
import io.fiap.revenda.documentos.driven.port.MessagingPort;
import io.fiap.revenda.documentos.driven.repository.DocumentoRepository;
import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class DocumentoService {
    private final String emitirDocumentoQueue;
    private final String veiculosVendaQueue;
    private final DocumentoRepository repository;
    private final DetranClient detranClient;
    private final MessagingPort messagingPort;
    private final ObjectMapper objectMapper;

    public DocumentoService(@Value("${aws.sqs.documentosEmitir.queue}")
                            String emitirDocumentoQueue,
                            @Value("${aws.sqs.veiculosVenda.queue}")
                            String veiculosVendaQueue,
                            DocumentoRepository repository,
                            DetranClient detranClient,
                            MessagingPort messagingPort,
                            ObjectMapper objectMapper) {
        this.repository = repository;
        this.detranClient = detranClient;
        this.emitirDocumentoQueue = emitirDocumentoQueue;
        this.veiculosVendaQueue = veiculosVendaQueue;
        this.messagingPort = messagingPort;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> updateEmitido(String id, Boolean emitido) {
        return repository.findById(id)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException("Documento não encontrado."))))
            .map(documento -> ImmutableDocumento.copyOf(documento).withEmitido(emitido))
            .flatMap(repository::save)
            .flatMap(documento -> this.confirmarVendaVeiculo(documento.getVeiculo().getId()))
            .then();
    }

    public Mono<Void> confirmarVendaVeiculo(String veiculoId) {
        return Mono.fromCallable(() -> ImmutableMarcarVeiculoVendidoRequestDTO.builder()
                .id(veiculoId)
                .build())
            .flatMap(request -> messagingPort.send(veiculosVendaQueue, request, serializePayload()))
            .then();
    }

    private <T> CheckedFunction1<T, String> serializePayload() {
        return objectMapper::writeValueAsString;
    }

    public Flux<Documento> fetch(Boolean emitido) {
        return repository.fetch(emitido);
    }

    public Flux<Message> handleEmitirDocumento() {
        return messagingPort.read(emitirDocumentoQueue, handle(), readEvent());
    }

    private CheckedFunction1<Message, EmitirDocumentoRequestDTO> readEvent() {
        return message -> objectMapper.readValue(message.body(), EmitirDocumentoRequestDTO.class);
    }

    private Function1<EmitirDocumentoRequestDTO, Mono<EmitirDocumentoRequestDTO>> handle() {
        return request -> Mono.fromCallable(() ->
                ImmutableDocumento.builder()
                    .id(request.getId())
                    .tipo(request.getTipo())
                    .orgao(request.getOrgao())
                    .emitido(false)
                    .veiculo(request.getVeiculo())
                    .pessoa(request.getPessoa())
                    .build()
            )
            .flatMap(repository::save)
            .flatMap(documento -> detranClient.emitirDetran(documento.getId()))
            .then(Mono.just(request));
    }
}
