package br.com.lczapparolli.mapper;

import static java.util.Objects.isNull;

import br.com.lczapparolli.dto.LayoutDTO;
import br.com.lczapparolli.entity.LayoutEntity;

/**
 * Métodos para mapeamento dos dados de layouts
 *
 * @author lczapparolli
 */
public final class LayoutMapper {

    /**
     * Converte um objeto de banco de dados de layout para um DTO
     *
     * @param layout Objeto a ser convertido
     * @return DTO com os dados do layout
     */
    public static LayoutDTO toDTO(LayoutEntity layout) {
        if (isNull(layout)) {
            return null;
        }

        return LayoutDTO.builder()
                .id(layout.getLayoutId())
                .descricao(layout.getDescricao())
                .ativo(layout.getAtivo())
                .build();
    }

}
