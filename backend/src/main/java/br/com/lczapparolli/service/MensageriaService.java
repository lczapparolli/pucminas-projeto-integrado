package br.com.lczapparolli.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.lczapparolli.dto.ComandoArquivoDTO;
import br.com.lczapparolli.dto.SituacaoImportacaoDTO;
import br.com.lczapparolli.entity.ImportacaoEntity;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * Serviço de encapsulamento da lógica de mensageria
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class MensageriaService {

    @Inject
    ImportacaoService importacaoService;

    @Inject
    @Channel("comandos-arquivos")
    Emitter<ComandoArquivoDTO> comandoEmitter;

    /**
     * Recebe os eventos de atualização de situação das importações
     *
     * @param jsonObject Conteúdo da mensagem, contendo a situação atualizada
     * @return Retorna o objeto recebido para ser reaproveitado
     */
    @Blocking
    @Incoming("eventos-arquivos")
    @Outgoing("atualizacao-processamento-out")
    public SituacaoImportacaoDTO receberAtualizacao(JsonObject jsonObject) {
        var atualizacao = jsonObject.mapTo(SituacaoImportacaoDTO.class);
        importacaoService.atualizarStatusImportacao(atualizacao.getImportacaoId(), atualizacao.getSituacao());
        return atualizacao;
    }

    /**
     * Envia um comando para as rotinas de importação
     *
     * @param comando Comando a ser enviado
     * @param importacao Dados da importação para definição do destino da mensagem
     */
    public void enviarComando(ComandoArquivoDTO comando, ImportacaoEntity importacao) {
        var metadadata = OutgoingRabbitMQMetadata.builder()
                .withRoutingKey(String.format("%s-%d", importacao.getLayout().getIdentificacao(), importacao.getImportacaoId()))
                .build();
        var mensagem = Message.of(comando, Metadata.of(metadadata));
        comandoEmitter.send(mensagem);
    }
}
