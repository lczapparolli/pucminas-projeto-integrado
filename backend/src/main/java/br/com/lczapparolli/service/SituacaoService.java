package br.com.lczapparolli.service;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.lczapparolli.dominio.Situacao;
import br.com.lczapparolli.entity.SituacaoEntity;
import br.com.lczapparolli.repository.SituacaoRepository;

/**
 * Serviço para manipulação dos dados das situações
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class SituacaoService {

    @Inject
    SituacaoRepository situacaoRepository;

    /**
     * Realiza a consulta da situação pelo item do enumerador correspondente
     *
     * @param situacao Enumerador correspondente à situação a ser buscada
     * @return Retorna a situação encontrada
     */
    public Optional<SituacaoEntity> obterSituacaoEntity(Situacao situacao) {
        return situacaoRepository.encontrarPorDescricao(situacao.name());
    }

}
