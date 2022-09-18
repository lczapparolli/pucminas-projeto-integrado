package br.com.lczapparolli;

import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.repository.LayoutRepository;

/**
 * Classe de utilidades para manipulação dos dados de layout durante os testes
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class LayoutTestUtils {

    private final AtomicInteger contador = new AtomicInteger();

    @Inject
    LayoutRepository layoutRepository;

    /**
     * Cria um layout ativo salvando os dados no banco de dados
     *
     * @return Retorna a instância criada
     */
    @Transactional
    public LayoutEntity gerarNovoLayout() {
        return gerarNovoLayout(true);
    }

    /**
     * Cria um layout com a situação informada, salvando os dados no banco de dados
     *
     * @param ativo Indicação se o layout deve estar ativo
     * @return Retrona a instância criada
     */
    @Transactional
    public LayoutEntity gerarNovoLayout(boolean ativo) {
        var indice = contador.incrementAndGet();

        var layout = LayoutEntity.builder()
                .identificacao(String.format("layout_%05d", indice))
                .descricao(String.format("Layout %05d", indice))
                .ativo(ativo)
                .build();
        layoutRepository.persist(layout);
        return layout;
    }

}
