package io.fiap.revenda.documentos.driven.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDetranRequestDTO.class)
@JsonDeserialize(as = ImmutableDetranRequestDTO.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class DetranRequestDTO {
    public abstract String getId();
    public abstract String getWebhook();
}