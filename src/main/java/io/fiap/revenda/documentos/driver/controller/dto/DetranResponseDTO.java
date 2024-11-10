package io.fiap.revenda.documentos.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDetranResponseDTO.class)
@JsonDeserialize(as = ImmutableDetranResponseDTO.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class DetranResponseDTO {
    public abstract String getId();
    public abstract String getWebhook();
    public abstract Boolean getEmitido();
}