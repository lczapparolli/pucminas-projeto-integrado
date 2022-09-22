package br.com.lczapparolli.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Dados do layout a serem expostos
 */
@Data
@Builder
public class LayoutDTO {

    /**
     * Identificação do layout
     */
    private Integer id;

    /**
     * Descrição legível
     */
    private String descricao;

    /**
     * Identificação se o layout está ativo ou não
     */
    private Boolean ativo;

}
