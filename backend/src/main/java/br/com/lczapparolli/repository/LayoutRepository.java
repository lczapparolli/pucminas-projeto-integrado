package br.com.lczapparolli.repository;

import javax.enterprise.context.ApplicationScoped;

import br.com.lczapparolli.entity.LayoutEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

/**
 * Repositório para manipulação dos dados de layouts
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class LayoutRepository implements PanacheRepositoryBase<LayoutEntity, Integer> {
}
