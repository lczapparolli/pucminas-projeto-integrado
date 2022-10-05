package br.com.lczapparolli.repository;

import static java.util.Objects.isNull;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import br.com.lczapparolli.entity.ImportacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

/**
 * Repositório para manipulação dos dados de importações
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class ImportacaoRepository implements PanacheRepositoryBase<ImportacaoEntity, Integer> {

    /**
     * Consulta as importações de acordo com a lista de situações.
     * Caso o parâmetro se nulo ou uma lista vazia, retorna todas as importações
     *
     * @param situacoes Situações a serem pesquisadas
     * @return A lista de importações encontradas
     */
    public List<ImportacaoEntity> listarPorSituacoes(List<String> situacoes) {
        if (isNull(situacoes) || situacoes.isEmpty()) {
            return listAll();
        }

        var parametros = Parameters.with("situacoes", situacoes);
        return list("situacao.descricao in :situacoes", parametros);
    }

}
