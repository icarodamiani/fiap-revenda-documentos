package io.fiap.revenda.documentos.driven.repository;

import io.fiap.revenda.documentos.driven.domain.Documento;
import io.fiap.revenda.documentos.driven.domain.ImmutableDocumento;
import io.fiap.revenda.documentos.driven.domain.ImmutableDocumentoPessoa;
import io.fiap.revenda.documentos.driven.domain.ImmutablePessoa;
import io.fiap.revenda.documentos.driven.domain.ImmutableVeiculo;
import io.fiap.revenda.documentos.driven.domain.Pessoa;
import io.fiap.revenda.documentos.driven.domain.Veiculo;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Repository
public class DocumentoRepository {
    private static final String TABLE_NAME = "documentos_tb";

    private final DynamoDbAsyncClient client;

    public DocumentoRepository(DynamoDbAsyncClient client) {
        this.client = client;
    }

    public Mono<Documento> save(Documento documento) {
        var atributos = new HashMap<String, AttributeValueUpdate>();
        atributos.put("ORGAO",
            AttributeValueUpdate.builder().value(v -> v.s(documento.getOrgao()).build()).build());
        atributos.put("TIPO",
            AttributeValueUpdate.builder().value(v -> v.s(documento.getTipo()).build()).build());
        atributos.put("EMITIDO",
            AttributeValueUpdate.builder().value(v -> v.s(documento.getEmitido().toString()).build()).build());

        atributos.put("PESSOA",
            AttributeValueUpdate.builder().value(v -> v.m(mapPessoa(documento.getPessoa())).build()).build());

        atributos.put("VEICULO",
            AttributeValueUpdate.builder().value(v -> v.m(mapVeiculo(documento.getVeiculo())).build()).build());

        var request = UpdateItemRequest.builder()
            .attributeUpdates(atributos)
            .tableName(TABLE_NAME)
            .key(Map.of("ID", AttributeValue.fromS(documento.getId())))
            .build();

        return Mono.fromFuture(client.updateItem(request))
            .then(Mono.just(documento));
    }

    public Flux<Documento> fetch(Boolean emitido) {
        var request = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .indexName("EmitidoIndex")
            .keyConditionExpression("#emitido = :emitido")
            .expressionAttributeNames(Map.of("#emitido", "EMITIDO"))
            .expressionAttributeValues(Map.of(":emitido", AttributeValue.fromS(emitido.toString())))
            .build();

        return Mono.fromFuture(client.query(request))
            .filter(QueryResponse::hasItems)
            .map(response -> response.items()
                .stream()
                .map(this::convertItem)
                .toList()
            )
            .flatMapIterable(l -> l);
    }

    public Mono<Documento> findById(String id) {
        var request = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .keyConditionExpression("#id = :id")
            .expressionAttributeNames(Map.of("#id", "ID"))
            .expressionAttributeValues(Map.of(":id", AttributeValue.fromS(id)))
            .build();

        return Mono.fromFuture(client.query(request))
            .filter(QueryResponse::hasItems)
            .map(response -> response.items().get(0))
            .map(this::convertItem);
    }

    private HashMap<String, AttributeValue> mapPessoa(Pessoa pessoa) {
        var pessoaAttr = new HashMap<String, AttributeValue>();
        pessoaAttr.put("ID", AttributeValue.builder().s(pessoa.getId()).build());
        pessoaAttr.put("NOME", AttributeValue.builder().s(pessoa.getNome()).build());
        pessoaAttr.put("SOBRENOME", AttributeValue.builder().s(pessoa.getSobrenome()).build());

        var documentoPessoaAttr = new HashMap<String, AttributeValue>();
        documentoPessoaAttr.put("TIPO", AttributeValue.builder().s(pessoa.getDocumento().getTipo()).build());
        documentoPessoaAttr.put("VALOR", AttributeValue.builder().s(pessoa.getDocumento().getValor()).build());

        pessoaAttr.put("DOCUMENTO", AttributeValue.builder().m(documentoPessoaAttr).build());

        return pessoaAttr;
    }

    private HashMap<String, AttributeValue> mapVeiculo(Veiculo veiculo) {
        var atributos = new HashMap<String, AttributeValue>();
        atributos.put("ID",
            AttributeValue.builder().s(veiculo.getId()).build());
        atributos.put("COR",
            AttributeValue.builder().s(veiculo.getCor()).build());
        atributos.put("ANO",
            AttributeValue.builder().s(veiculo.getAno()).build());
        atributos.put("MARCA",
            AttributeValue.builder().s(veiculo.getMarca()).build());
        atributos.put("PLACA",
            AttributeValue.builder().s(veiculo.getPlaca()).build());
        atributos.put("CAMBIO",
            AttributeValue.builder().s(veiculo.getCambio()).build());
        atributos.put("RENAVAM",
            AttributeValue.builder().s(veiculo.getRenavam()).build());
        atributos.put("MODELO",
            AttributeValue.builder().s(veiculo.getModelo()).build());

        return atributos;
    }

    private Documento convertItem(Map<String, AttributeValue> item) {
        Map<String, AttributeValue> veiculoAttr = item.get("VEICULO").m();

        var veiculo = ImmutableVeiculo.builder()
            .id(veiculoAttr.get("ID").s())
            .ano(veiculoAttr.get("ANO").s())
            .cor(veiculoAttr.get("COR").s())
            .placa(veiculoAttr.get("PLACA").s())
            .marca(veiculoAttr.get("MARCA").s())
            .cambio(veiculoAttr.get("CAMBIO").s())
            .modelo(veiculoAttr.get("MODELO").s())
            .renavam(veiculoAttr.get("RENAVAM").s())
            .build();

        Map<String, AttributeValue> pessoaAttr = item.get("PESSOA").m();
        var pessoa = ImmutablePessoa.builder()
            .id(pessoaAttr.get("ID").s())
            .nome(pessoaAttr.get("NOME").s())
            .sobrenome(pessoaAttr.get("SOBRENOME").s())
            .documento(ImmutableDocumentoPessoa.builder()
                .tipo(pessoaAttr.get("DOCUMENTO").m().get("TIPO").s())
                .valor(pessoaAttr.get("DOCUMENTO").m().get("VALOR").s())
                .build()
            )
            .build();

        return ImmutableDocumento.builder()
            .id(item.get("ID").s())
            .tipo(item.get("TIPO").s())
            .orgao(item.get("ORGAO").s())
            .emitido(Boolean.valueOf(item.get("EMITIDO").s()))
            .pessoa(pessoa)
            .veiculo(veiculo)
            .build();
    }
}
