package io.fiap.revenda.documentos.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fiap.revenda.documentos.driven.domain.ImmutableDocumentoPessoa;
import java.time.LocalDate;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDocumentoPessoa.class)
@JsonDeserialize(as = ImmutableDocumentoPessoa.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class DocumentoPessoaDTO {
    public abstract String getTipo();
    public abstract String getValor();
    public abstract LocalDate getExpiracao();
}
