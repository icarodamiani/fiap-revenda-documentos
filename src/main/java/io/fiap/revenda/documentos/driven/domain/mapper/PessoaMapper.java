package io.fiap.revenda.documentos.driven.domain.mapper;

import io.fiap.revenda.documentos.driven.domain.Pessoa;
import io.fiap.revenda.documentos.driver.controller.dto.PessoaDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentoPessoaMapper.class})
public interface PessoaMapper extends BaseMapper<PessoaDTO, Pessoa> {
}
