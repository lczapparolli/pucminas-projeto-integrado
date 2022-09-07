package br.com.lczapparolli.service;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.repository.LayoutRepository;

/**
 * Serviço com as regras de negócio para manipulação dos layouts
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class LayoutService {

    @Inject
    LayoutRepository layoutRepository;

    /**
     * Obtém os dados de um layout específico
     *
     * @param layoutId Identificação do layout
     * @return Retorna o layout encontrado
     */
    public Optional<LayoutEntity> obterLayout(Integer layoutId) {
        return layoutRepository.findByIdOptional(layoutId);
    }

}
