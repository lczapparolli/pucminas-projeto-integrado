package br.com.lczapparolli.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.erro.ErroAplicacao;
import br.com.lczapparolli.erro.ResultadoOperacao;
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
    public ResultadoOperacao<LayoutEntity> obterLayout(Integer layoutId) {
        var resultado = ResultadoOperacao.<LayoutEntity>builder();

        var layoutOpcional = layoutRepository.findByIdOptional(layoutId);
        if (layoutOpcional.isEmpty()) {
            resultado.erro(ErroAplicacao.ERRO_LAYOUT_NAO_ENCONTRADO);
        }

        resultado.resultado(layoutOpcional.orElse(null));

        return resultado.build();
    }

}
