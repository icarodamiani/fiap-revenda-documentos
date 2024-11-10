package io.fiap.revenda.documentos.driven.domain.mapper;

import io.fiap.revenda.documentos.driven.domain.Veiculo;
import io.fiap.revenda.documentos.driver.controller.dto.VeiculoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeiculoMapper extends BaseMapper<VeiculoDTO, Veiculo> {
}
