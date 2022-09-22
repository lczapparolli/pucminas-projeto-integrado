package br.com.lczapparolli.service;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.lczapparolli.entity.LayoutEntity;
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
    public Optional<LayoutEntity> obterLayout(Integer layoutId) {
        return layoutRepository.findByIdOptional(layoutId);
    }

    /**
     * Obtém a lista de layouts cadastrados
     *
     * @param ativo Indicação se a consulta deve retornar apenas os layouts ativos ou inativos. Caso seja nulo, todos os layouts são retornados
     * @return Retorna a lista encontrada
     */
    public ResultadoOperacao<List<LayoutEntity>> listarLayouts(Boolean ativo) {
        var resultado = new ResultadoOperacao<List<LayoutEntity>>();

        if (isNull(ativo)) {
            resultado.setResultado(layoutRepository.listAll());
        } else {
            resultado.setResultado(layoutRepository.list("ativo", ativo));
        }

        return resultado;
    }

}
