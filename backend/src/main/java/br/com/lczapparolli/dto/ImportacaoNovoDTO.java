package br.com.lczapparolli.dto;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe para transferência dos dados de uma nova importação
 *
 * @author lczapparolli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportacaoNovoDTO {

    /**
     * Identificação do layout a ser utilizado para a importação
     */
    private Integer layoutId;

    /**
     * Nome do arquivo a ser importado
     */
    private String nomeArquivo;

    /**
     * Conteúdo do arquivo a ser importado
     */
    private InputStream arquivo;

}
