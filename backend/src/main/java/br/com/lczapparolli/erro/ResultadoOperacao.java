package br.com.lczapparolli.erro;

import java.util.ArrayList;
import java.util.List;

import br.com.lczapparolli.dto.ErroDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Classe para retornar o resultado de uma operação do serviço
 *
 * @param <T> Classe do objeto para o resultado de sucesso
 * @author lczapparolli
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoOperacao<T> {

    /**
     * Resultado de sucesso da operação
     */
    private T resultado;

    /**
     * Lista de erros que ocorreram durante a operação
     */
    private final List<ErroDTO> erros = new ArrayList<>();

    /**
     * Indica se houve algum erro ao realizar o processamento
     *
     * @return Retorna @{@code true} se houver algum erro
     */
    public boolean possuiErros() {
        return !erros.isEmpty();
    }

    /**
     * Define o resultado da operação
     *
     * @param resultado Resultado a ser definido
     * @return Retorna a instância atualizada do objeto para encadeamento
     */
    public ResultadoOperacao<T> setResultado(T resultado) {
        this.resultado = resultado;
        return this;
    }

    /**
     * Adiciona um erro na lista
     *
     * @param erro Erro a ser adicionado
     * @return Retorna a instância atualizada do objeto para encadeamento
     */
    public ResultadoOperacao<T> addErro(ErroDTO erro) {
        erros.add(erro);
        return this;
    }

    /**
     * Adiciona um erro na lista a partir de um item do enumerador {@link ErroAplicacao}
     *
     * @param erroAplicacao Item do enumerador contendo os dados do erro
     * @return Retorna a instância atualizada do objeto para encadeamento
     */
    public ResultadoOperacao<T> addErro(ErroAplicacao erroAplicacao) {
        return addErro(erroAplicacao, null);
    }

    /**
     * Adiciona um erro na lista a partir de um item do enumerador {@link ErroAplicacao} e vinculando a um campo
     *
     * @param erroAplicacao Item do enumerador contendo os dados do erro
     * @param campo Campo associado ao erro gerado
     * @return Retorna a instância atualizada do objeto para encadeamento
     */
    public ResultadoOperacao<T> addErro(ErroAplicacao erroAplicacao, String campo) {
        return addErro(ErroDTO.builder()
                .mensagem(erroAplicacao.getMensagem())
                .validacao(erroAplicacao.isValidacao())
                .codigo(erroAplicacao.getCodigoErro())
                .campo(campo)
                .build());
    }

}
