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

    ERRO_DESCONHECIDO("Ocorreu um erro desconhecido na aplicação", false),
    ERRO_IMPORTACAO_NAO_INFORMADA("Os dados para importação não foram informados", true),
    ERRO_LAYOUT_NAO_ENCONTRADO("O layout informado não existe", true),
    ERRO_CAMPO_NAO_INFORMADO("O campo não foi informado", true),
    ERRO_LAYOUT_DESATIVADO("O layout informado está desativado e não pode ser utilizado", true),
    ERRO_UPLOAD_CONECTAR("Não foi possível conectar ao serviço de armazenamento de arquivos", false),
    ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO("Não foi possível encontrar o arquivo carregado para enviar ao serviço de armazenamento", false),
    ERRO_UPLOAD_TRANSMISSAO("Ocorreu um erro ao enviar o arquivo para o serviço de armazenamento", false);

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
