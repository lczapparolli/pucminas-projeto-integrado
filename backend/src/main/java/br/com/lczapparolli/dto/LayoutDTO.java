package br.com.lczapparolli.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Dados do layout a serem expostos
 */
@Data
@EqualsAndHashCode
@Builder
@Schema(name = "Layout", description = "Dados do layout para importação")
public class LayoutDTO {

    /**
     * Identificação única do Layout
     */
    @Schema(name = "id", description = "Identificação única do Layout", example = "1", required = true)
    private Integer id;

    /**
     * Descrição legível
     */
    @Schema(name = "descricao", description = "Descrição legível", example = "Layout de testes", required = true)
    private String descricao;

    /**
     * Identificação se o layout está ativo ou não
     */
    @Schema(name = "ativo", description = "Identificação se o layout está tivo ou não", example = "true", required = true)
    private Boolean ativo;

}
