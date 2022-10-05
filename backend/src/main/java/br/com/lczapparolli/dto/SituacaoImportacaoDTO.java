package br.com.lczapparolli.dto;

import br.com.lczapparolli.dominio.Situacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Classe com os dados atualizados da situação de uma importação
 *
 * @author lczapparolli
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(name = "SituacaoImportacao", description = "Dados atualizados da situação de uma importação")
public class SituacaoImportacaoDTO {

    /**
     * Identificação da importação
     */
    @Schema(name = "importacaoId", description = "Identificação da importação", example = "1", required = true)
    private Integer importacaoId;

    /**
     * Tamanho do arquivo (em bytes)
     */
    @Schema(name = "tamanho", description = "Tamanho do arquivo (em bytes)", example = "14785", required = true)
    private Long tamanho;

    /**
     * Quantidade de bytes lidos (aproximadamente)
     */
    @Schema(name = "posicao", description = "Quantidade de bytes lidos (aproximadamente)", example = "14785", required = true)
    private Long posicao;

    /**
     * Situação da importação
     */
    @Schema(name = "situacao", description = "Situação da importação", example = "PROCESSANDO", required = true)
    private Situacao situacao;

}
