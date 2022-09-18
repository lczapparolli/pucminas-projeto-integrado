package br.com.lczapparolli.repository;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;

import br.com.lczapparolli.entity.SituacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Repositório para manipulação dos dados de situações
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class SituacaoRepository implements PanacheRepositoryBase<SituacaoEntity, Integer> {

    /**
     * Carrega a entidade da situação correspondente à descrição informada
     *
     * @param descricao Descrição da situação a ser pesquisada
     * @return Retorna a situação encontrada
     */
    public Optional<SituacaoEntity> encontrarPorDescricao(String descricao) {
        return find("descricao", descricao).singleResultOptional();
    }

}
