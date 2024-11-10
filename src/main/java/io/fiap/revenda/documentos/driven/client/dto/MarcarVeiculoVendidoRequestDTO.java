package io.fiap.revenda.documentos.driven.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableMarcarVeiculoVendidoRequestDTO.class)
@JsonDeserialize(as = ImmutableMarcarVeiculoVendidoRequestDTO.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class MarcarVeiculoVendidoRequestDTO {
    public abstract String getId();
}
