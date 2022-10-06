package br.com.lczapparolli.erro;

import static br.com.lczapparolli.constantes.Constantes.Erro.PREFIXO_EXCECAO;
import static br.com.lczapparolli.constantes.Constantes.Erro.PREFIXO_VALIDACAO;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.Response;

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

    ERRO_DESCONHECIDO("Ocorreu um erro desconhecido na aplicação", INTERNAL_SERVER_ERROR),
    ERRO_IMPORTACAO_NAO_INFORMADA("Os dados para importação não foram informados", BAD_REQUEST),
    ERRO_LAYOUT_NAO_ENCONTRADO("O layout informado não existe", BAD_REQUEST),
    ERRO_CAMPO_NAO_INFORMADO("O campo não foi informado", BAD_REQUEST),
    ERRO_LAYOUT_DESATIVADO("O layout informado está desativado e não pode ser utilizado", BAD_REQUEST),
    ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO("Não foi possível encontrar o arquivo carregado para enviar ao serviço de armazenamento", INTERNAL_SERVER_ERROR),
    ERRO_IMPORTACAO_NAO_ENCONTRADA("Não foi possível encontrar a importação com o ID informado", NOT_FOUND),
    ERRO_IMPORTACAO_NAO_PROCESSANDO("Não é possível executar essa ação porque a importação não está sendo processada", BAD_REQUEST);

    /**
     * Mensagem associada ao erro para ser devolvida ao chamador
     */
    private final String mensagem;

    /**
     * Indica se o erro foi causado por conta de uma informação inválida enviada pelo usuário
     */
    private final Response.Status statusHttp;

    /**
     * Formata o código do erro, gerando um identificador
     *
     * @return Retorna o código gerado
     */
    public String getCodigoErro() {
        var codigo = String.format("%05d", ordinal());
        var prefixo = INTERNAL_SERVER_ERROR.equals(statusHttp) ? PREFIXO_EXCECAO : PREFIXO_VALIDACAO;
        return String.format("%s_%s", prefixo, codigo);
    }

}
