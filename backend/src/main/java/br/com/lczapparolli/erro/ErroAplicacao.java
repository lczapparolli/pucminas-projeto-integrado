package br.com.lczapparolli.erro;

import static br.com.lczapparolli.constantes.Constantes.Erro.PREFIXO_EXCECAO;
import static br.com.lczapparolli.constantes.Constantes.Erro.PREFIXO_VALIDACAO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerador com os possíveis erros da aplicação
 *
 * @author lczapparolli
 */
@Getter
@AllArgsConstructor
public enum ErroAplicacao {

    ERRO_DESCONHECIDO("Ocorreu um erro desconhecido na aplicação", false);

    /**
     * Mensagem associada ao erro para ser devolvida ao chamador
     */
    private final String mensagem;

    /**
     * Indica se o erro foi causado por conta de uma informação inválida enviada pelo usuário
     */
    private final boolean validacao;

    /**
     * Formata o código do erro, gerando um identificador
     *
     * @return Retorna o código gerado
     */
    public String getCodigoErro() {
        var codigo = String.format("%05d", ordinal());
        var prefixo = validacao ? PREFIXO_VALIDACAO : PREFIXO_EXCECAO;
        return String.format("%s_%s", prefixo, codigo);
    }

}
