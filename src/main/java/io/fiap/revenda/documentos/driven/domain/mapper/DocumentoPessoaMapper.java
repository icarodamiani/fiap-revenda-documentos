package io.fiap.revenda.documentos.driven.domain.mapper;

import io.fiap.revenda.documentos.driven.domain.DocumentoPessoa;
import io.fiap.revenda.documentos.driver.controller.dto.DocumentoPessoaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentoPessoaMapper.class})
public interface DocumentoPessoaMapper extends BaseMapper<DocumentoPessoaDTO, DocumentoPessoa> {
}
