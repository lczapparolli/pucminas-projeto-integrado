package br.com.lczapparolli.dto;

import java.time.LocalDateTime;

import br.com.lczapparolli.dominio.Situacao;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Dados da importação a serem expostos
 *
 * @author lczapparolli
 */
@Data
@EqualsAndHashCode
@Builder
@Schema(name = "ImportacaoDetalhada", description = "Dados da importação")
public class ImportacaoDetalhadaDTO {

    /**
     * Identificação única da importação
     */
    @Schema(name = "id", description = "Identificação única da importação", example = "1", required = true)
    private Integer id;

    /**
     * Dados do layout associado à importação
     */
    @Schema(name = "layout", description = "Dados do layout associado à importação", required = true)
    private LayoutDTO layout;

    /**
     * Nome do arquivo importado
     */
    @Schema(name = "nomeArquivo", description = "Nome do arquivo importado", example = "arquivo1.txt", required = true)
    private String nomeArquivo;

    /**
     * Situação do processamento
     */
    @Schema(name = "situacao", description = "Situação do processamento", example = "PROCESSANDO", required = true)
    private Situacao situacao;

    /**
     * Data e hora em que a importação iniciou
     */
    @Schema(name = "dataHoraInicio", description = "Data e hora em que a importação iniciou", example = "2022-10-02T15:24:50", required = true)
    private LocalDateTime dataHoraInicio;

    /**
     * Data e hora em que a importação foi encerrada
     */
    @Schema(name = "dataHoraFim", description = "Data e hora em que a importação foi encerrada", example = "2022-10-02T15:24:50")
    private LocalDateTime dataHoraFim;

}
