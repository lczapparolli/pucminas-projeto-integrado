package br.com.lczapparolli.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.lczapparolli.entity.ImportacaoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Repositório para manipulação dos dados de importações
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class ImportacaoRepository implements PanacheRepositoryBase<ImportacaoEntity, Integer> {
}
