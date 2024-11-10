package io.fiap.revenda.documentos.driven.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableEmitirDocumentoResponseDTO.class)
@JsonDeserialize(as = ImmutableEmitirDocumentoResponseDTO.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class EmitirDocumentoResponseDTO {
    public abstract String getId();
}