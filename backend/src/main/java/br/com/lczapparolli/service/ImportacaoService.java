package br.com.lczapparolli.service;

import static br.com.lczapparolli.dominio.Situacao.CANCELADA;
import static br.com.lczapparolli.dominio.Situacao.ERRO;
import static br.com.lczapparolli.dominio.Situacao.PAUSADA;
import static br.com.lczapparolli.dominio.Situacao.PROCESSANDO;
import static br.com.lczapparolli.dominio.Situacao.SUCESSO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_CAMPO_NAO_INFORMADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_IMPORTACAO_NAO_ENCONTRADA;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_IMPORTACAO_NAO_INFORMADA;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_DESATIVADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_LAYOUT_NAO_ENCONTRADO;
import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.lczapparolli.dominio.Situacao;
import br.com.lczapparolli.dto.ImportacaoNovoDTO;
import br.com.lczapparolli.dto.SituacaoImportacaoDTO;
import br.com.lczapparolli.entity.ImportacaoEntity;
import br.com.lczapparolli.entity.LayoutEntity;
import br.com.lczapparolli.entity.SituacaoEntity;
import br.com.lczapparolli.erro.ResultadoOperacao;
import br.com.lczapparolli.repository.ImportacaoRepository;
import br.com.lczapparolli.service.upload.UploadService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * Serviço com regras de negócio para manipulação das importações
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class ImportacaoService {

    @Inject
    LayoutService layoutService;

    @Inject
    SituacaoService situacaoService;

    @Inject
    UploadService uploadService;

    @Inject
    ImportacaoRepository importacaoRepository;

    private static final List<Situacao> fimProcessamento = List.of(SUCESSO, ERRO, CANCELADA);
    private static final List<Situacao> processamentoAtivo = List.of(PROCESSANDO, PAUSADA);

    /**
     * Inicia o processo de importação, salvando os dados necessários no banco de dados
     *
     * @param importacaoDTO Dados para o início da importação
     * @return Retorna o resultado da operação com os erros encontrados
     */
    @Transactional
    public ResultadoOperacao<Void> iniciarImportacao(ImportacaoNovoDTO importacaoDTO) {
        var resultado = validarImportacao(importacaoDTO);
        if (resultado.possuiErros()) {
            return resultado;
        }

        var consultaLayout = obterLayout(importacaoDTO.getLayoutId());
        if (consultaLayout.possuiErros()) {
            resultado.addErros(consultaLayout.getErros());
            return resultado;
        }

        var importacaoEntity = ImportacaoEntity.builder()
                .layout(consultaLayout.getResultado())
                .nomeArquivo(importacaoDTO.getNomeArquivo())
                .inicioImportacao(LocalDateTime.now())
                .situacao(obterSituacao(PROCESSANDO))
                .build();
        importacaoRepository.persist(importacaoEntity);

        var upload = uploadService.carregarArquivo(
                importacaoEntity.getLayout().getIdentificacao(),
                importacaoEntity.getImportacaoId(),
                importacaoDTO.getArquivo());
        if (upload.possuiErros()) {
            importacaoEntity.setSituacao(obterSituacao(ERRO));
            importacaoRepository.persist(importacaoEntity);

            resultado.addErros(upload.getErros());
        }

        return resultado;
    }

    /**
     * Lista as importações conforme o filtro informado
     *
     * @param ativo Indica se devem ser filtradas apenas as importações ativas ou finalizadas.
     *              Se nulo todas as importações são retornadas
     * @return Retorna a lista de importações encontradas
     */
    public ResultadoOperacao<List<ImportacaoEntity>> listarImportacoes(Boolean ativo) {
        var resultadoOperacao = new ResultadoOperacao<List<ImportacaoEntity>>();

        if (isNull(ativo)) {
            resultadoOperacao.setResultado(importacaoRepository.listarPorSituacoes(null));
        } else if (ativo) {
            resultadoOperacao.setResultado(importacaoRepository.listarPorSituacoes(obterDescricaoSituacoes(processamentoAtivo)));
        } else {
            resultadoOperacao.setResultado(importacaoRepository.listarPorSituacoes(obterDescricaoSituacoes(fimProcessamento)));
        }

        return resultadoOperacao;
    }

    /**
     * Consulta uma única importação pelo ID informado
     *
     * @param importacaoId Identificação da importação a ser consultado
     * @return Retorna os dados da importação
     */
    public ResultadoOperacao<ImportacaoEntity> obterImportacao(Integer importacaoId) {
        var resultadoOperacao = new ResultadoOperacao<ImportacaoEntity>();

        var importacaoOptional = importacaoRepository.findByIdOptional(importacaoId);
        if (importacaoOptional.isEmpty()) {
            resultadoOperacao.addErro(ERRO_IMPORTACAO_NAO_ENCONTRADA, "importacaoId");
            return resultadoOperacao;
        }

        resultadoOperacao.setResultado(importacaoOptional.get());
        return resultadoOperacao;
    }

    /**
     * Realiza o processamento das atualizações de situação das importações
     *
     * @param jsonObject Conteúdo da mensagem, contendo a situação atualizada
     * @return Retorna o objeto recebido para ser reaproveitado
     */
    @Blocking
    @Incoming("eventos-arquivos")
    @Outgoing("atualizacao-processamento-out")
    public SituacaoImportacaoDTO receberAtualizacao(JsonObject jsonObject) {
        var atualizacao = jsonObject.mapTo(SituacaoImportacaoDTO.class);
        atualizarStatusImportacao(atualizacao.getImportacaoId(), atualizacao.getSituacao());
        return atualizacao;
    }

    /**
     * Salva os dados da atualização da situação no banco de dados
     *
     * @param importacaoId Identificação da importação
     * @param situacao Situação atual da importação
     */
    @Transactional
    void atualizarStatusImportacao(Integer importacaoId, Situacao situacao) {
        var importacao = importacaoRepository.findById(importacaoId);
        if (importacao.getSituacao().getDescricao().equals(situacao.name())) {
            return;
        }

        importacao.setSituacao(obterSituacao(situacao));
        if (fimProcessamento.contains(situacao)) {
            importacao.setFimImportacao(LocalDateTime.now());
        }
        importacaoRepository.persist(importacao);
    }

    /**
     * Valida os dados para a importação
     *
     * @param importacaoDTO Dados recebidos para iniciação da importação
     * @return Retorna o resultado da validação com os erros encontrados
     */
    private ResultadoOperacao<Void> validarImportacao(ImportacaoNovoDTO importacaoDTO) {
        var resultado = new ResultadoOperacao<Void>();

        if (isNull(importacaoDTO)) {
            resultado.addErro(ERRO_IMPORTACAO_NAO_INFORMADA, null);
            return resultado;
        }

        if (isNull(importacaoDTO.getLayoutId()) || importacaoDTO.getLayoutId() == 0) {
            resultado.addErro(ERRO_CAMPO_NAO_INFORMADO, "layoutId");
        }

        if (isNull(importacaoDTO.getNomeArquivo()) || importacaoDTO.getNomeArquivo().isBlank()) {
            resultado.addErro(ERRO_CAMPO_NAO_INFORMADO, "nomeArquivo");
        }

        if (isNull(importacaoDTO.getArquivo())) {
            resultado.addErro(ERRO_CAMPO_NAO_INFORMADO, "arquivo");
        }

        return resultado;
    }

    /**
     * Obtém o objeto de layout correspondente ao id informado
     *
     * @param layoutId Identificação do layout a ser utilizado
     * @return Retorna o resultado da operação com o objeto consultado
     */
    private ResultadoOperacao<LayoutEntity> obterLayout(Integer layoutId) {
        var resultado = new ResultadoOperacao<LayoutEntity>();

        var layoutOptional = layoutService.obterLayout(layoutId);
        if (layoutOptional.isEmpty()) {
            resultado.addErro(ERRO_LAYOUT_NAO_ENCONTRADO, "layoutId");
            return resultado;
        }

        var layout = layoutOptional.get();
        if (!layout.getAtivo()) {
            resultado.addErro(ERRO_LAYOUT_DESATIVADO, "layoutId");
            return resultado;
        }

        resultado.setResultado(layout);

        return resultado;
    }

    /**
     * Obtém o objeto da situação referente ao item do enum informado
     *
     * @param situacao Situação a ser pesquisada na base
     * @return Retorna o objeto encontrado
     * @throws NoSuchElementException Dispara uma exceção caso o registro não seja encontrado
     */
    private SituacaoEntity obterSituacao(Situacao situacao) {
        // Considera-se que a situação exista na base,
        // por isso é disparada uma exceção em vez de realizar a validação
        return situacaoService.obterSituacaoEntity(situacao).orElseThrow();
    }

    private List<String> obterDescricaoSituacoes(List<Situacao> situacoes) {
        return situacoes.stream().map(Enum::name).collect(Collectors.toList());
    }

}
