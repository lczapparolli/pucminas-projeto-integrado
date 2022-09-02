package br.com.lczapparolli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Objeto contendo os dados de um erro que ocorreu na aplicação
 *
 * @author lczapparolli
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ErroDTO {

    /**
     * Identifica o campo que gerou o erro
     */
    private String campo;

    /**
     * Mensagem indicando o motivo do erro
     */
    private String mensagem;

    /**
     * Código do erro para rastreabilidade da causa
     */
    private String codigo;

    /**
     * Indicação se o erro foi causado por um dado inválido ou erro da aplicação
     */
    private boolean validacao;
}
