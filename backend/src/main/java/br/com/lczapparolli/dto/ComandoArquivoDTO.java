package br.com.lczapparolli.dto;

import br.com.lczapparolli.dominio.Comando;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe contendo os dados necessários para enviar um comando para as rotinas de importação
 *
 * @author lczapparolli
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ComandoArquivoDTO {

    /**
     * Comando a ser executado pela rotina de importação
     */
    private Comando comando;

}
