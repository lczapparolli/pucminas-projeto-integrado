package br.com.lczapparolli.erro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.lczapparolli.dto.ErroDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Classe para retornar o resultado de uma operação do serviço
 *
 * @param <T> Classe do objeto para o resultado de sucesso
 * @author lczapparolli
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ResultadoOperacao<T> {

    /**
     * Resultado de sucesso da operação
     */
    private T resultado;

    /**
     * Lista de erros que ocorreram durante a operação
     */
    private List<ErroDTO> erros;

    /**
     * Indica se houve algum erro ao realizar o processamento
     *
     * @return Retorna @{@code true} se houver algum erro
     */
    public boolean possuiErros() {
        return !Objects.isNull(erros) && !erros.isEmpty();
    }

    /**
     * Cria uma instância do construtor
     *
     * @return Retorna a instância criada
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Classe construtora para o resultado da operação
     *
     * @param <T> Classe do objeto para o resultado de sucesso
     * @author lczapparolli
     */
    public static class Builder<T> {

        /**
         * Resultado de sucesso da operação
         */
        private T resultado;

        /**
         * Lista de erros que ocorreram durante a operação
         */
        private final List<ErroDTO> erros = new ArrayList<>();

        /**
         * Define o resultado da operação
         *
         * @param resultado Resultado a ser definido
         * @return Retorna a instância atualizada do construtor
         */
        public Builder<T> resultado(T resultado) {
            this.resultado = resultado;
            return this;
        }

        /**
         * Adiciona um erro na lista
         *
         * @param erro Erro a ser adicionado
         * @return Retorna a instância atualizada do construtor
         */
        public Builder<T> erro(ErroDTO erro) {
            erros.add(erro);
            return this;
        }

        /**
         * Adiciona um erro na lista a partir de um item do enumerador {@link ErroAplicacao}
         *
         * @param erroAplicacao Item do enumerador contendo os dados do erro
         * @return Retorna a instância atualizada do construtor
         */
        public Builder<T> erro(ErroAplicacao erroAplicacao) {
            return erro(erroAplicacao, null);
        }

        /**
         * Adiciona um erro na lista a partir de um item do enumerador {@link ErroAplicacao} e vinculando a um campo
         *
         * @param erroAplicacao Item do enumerador contendo os dados do erro
         * @param campo Campo associado ao erro gerado
         * @return Retorna a instância atualizada do construtor
         */
        public Builder<T> erro(ErroAplicacao erroAplicacao, String campo) {
            return erro(ErroDTO.builder()
                    .mensagem(erroAplicacao.getMensagem())
                    .validacao(erroAplicacao.isValidacao())
                    .codigo(erroAplicacao.getCodigoErro())
                    .campo(campo)
                    .build());
        }

        /**
         * Constrói a instância da classe de resultado
         *
         * @return Retorna a instância criada
         */
        public ResultadoOperacao<T> build() {
            return new ResultadoOperacao<>(resultado, erros);
        }

    }

}
