package io.fiap.revenda.documentos.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fiap.revenda.documentos.driven.domain.ImmutablePessoa;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutablePessoa.class)
@JsonDeserialize(as = ImmutablePessoa.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class PessoaDTO {
    public abstract String getId();
    public abstract String getNome();
    public abstract String getSobrenome();
    public abstract DocumentoPessoaDTO getDocumento();
}
