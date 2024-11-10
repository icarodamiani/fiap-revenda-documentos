package io.fiap.revenda.documentos.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.fiap.revenda.documentos.driven.domain.ImmutableVeiculo;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableVeiculo.class)
@JsonDeserialize(as = ImmutableVeiculo.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class VeiculoDTO {
    public abstract String getId();
    public abstract String getAno();
    public abstract String getCor();
    public abstract String getPlaca();
    public abstract String getMarca();
    public abstract String getCambio();
    public abstract String getModelo();
    public abstract String getRenavam();
}
