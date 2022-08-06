package br.com.lczapparolli.repository;

import br.com.lczapparolli.entity.SituacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Repositório para manipulação dos dados de situações
 *
 * @author lczapparolli
 */
public class SituacaoRepository implements PanacheRepositoryBase<SituacaoEntity, Integer> {
}
