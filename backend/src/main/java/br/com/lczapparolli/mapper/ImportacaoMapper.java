package br.com.lczapparolli.mapper;

import static java.util.Objects.isNull;

import br.com.lczapparolli.dominio.Situacao;
import br.com.lczapparolli.dto.ImportacaoDetalhadaDTO;
import br.com.lczapparolli.dto.LayoutDTO;
import br.com.lczapparolli.entity.ImportacaoEntity;

/**
 * Métodos de mapeamento dos dados de importação
 *
 * @author lczapparolli
 */
public final class ImportacaoMapper {

    /**
     * Converte um objeto de banco de dados de importação para um DTO
     *
     * @param importacao Objeto a ser convertido
     * @return DTO com os dados da importação
     */
    public static ImportacaoDetalhadaDTO toDTO(ImportacaoEntity importacao) {
        if (isNull(importacao)) {
            return null;
        }

        return ImportacaoDetalhadaDTO.builder()
                .id(importacao.getImportacaoId())
                .layout(LayoutDTO.builder()
                    .id(importacao.getLayout().getLayoutId())
                    .descricao(importacao.getLayout().getDescricao())
                    .ativo(importacao.getLayout().getAtivo())
                    .build())
                .nomeArquivo(importacao.getNomeArquivo())
                .situacao(Situacao.valueOf(importacao.getSituacao().getDescricao()))
                .dataHoraInicio(importacao.getInicioImportacao())
                .dataHoraFim(importacao.getFimImportacao())
                .build();
    }

}
