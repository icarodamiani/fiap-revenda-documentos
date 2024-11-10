package io.fiap.revenda.documentos.driven.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableVeiculo.class)
@JsonDeserialize(as = ImmutableVeiculo.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class Veiculo {
    public abstract String getId();
    public abstract String getAno();
    public abstract String getCor();
    public abstract String getPlaca();
    public abstract String getMarca();
    public abstract String getCambio();
    public abstract String getModelo();
    public abstract String getRenavam();
}
