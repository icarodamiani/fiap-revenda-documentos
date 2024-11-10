package io.fiap.revenda.documentos.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fiap.revenda.documentos.driven.domain.ImmutableDocumento;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDocumento.class)
@JsonDeserialize(as = ImmutableDocumento.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class DocumentoDTO {
    public abstract String getId();
    public abstract String getTipo();
    public abstract String getOrgao();
    public abstract VeiculoDTO getVeiculo();
    public abstract PessoaDTO getPessoa();
    public abstract Boolean getEmitido();
}
