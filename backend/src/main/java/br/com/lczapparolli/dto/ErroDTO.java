package br.com.lczapparolli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Objeto contendo os dados de um erro que ocorreu na aplicação
 *
 * @author lczapparolli
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Schema(name = "Erro", description = "Dados do erro ocorrido")
public class ErroDTO {

    /**
     * Identifica o campo que gerou o erro
     */
    @Schema(name = "campo", description = "Identifica o campo que gerou o erro", example = "descricao")
    private String campo;

    /**
     * Mensagem indicando o motivo do erro
     */
    @Schema(name = "mensagem", description = "Mensagem indicando o motivo do erro", example = "O campo não está preenchido", required = true)
    private String mensagem;

    /**
     * Código do erro para rastreabilidade da causa
     */
    @Schema(name = "codigo", description = "Código do erro para rastreabilidade da causa", example = "USR_00001", required = true)
    private String codigo;

    /**
     * Indicação se o erro foi causado por um dado inválido ou erro da aplicação
     */
    @Schema(name = "validacao", description = "Indicação se o erro foi causado por um dado inválido ou erro da aplicação", example = "false", required = true)
    private boolean validacao;
}
