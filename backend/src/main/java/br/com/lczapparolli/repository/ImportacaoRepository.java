package br.com.lczapparolli.repository;

import br.com.lczapparolli.entity.ImportacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Repositório para manipulação dos dados de importações
 *
 * @author lczapparolli
 */
public class ImportacaoRepository implements PanacheRepositoryBase<ImportacaoEntity, Integer> {
}
