package io.fiap.revenda.documentos.driven.domain.mapper;

import io.fiap.revenda.documentos.driven.domain.Documento;
import io.fiap.revenda.documentos.driver.controller.dto.DocumentoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentoMapper.class, PessoaMapper.class, VeiculoMapper.class})
public interface DocumentoMapper extends BaseMapper<DocumentoDTO, Documento> {
}
