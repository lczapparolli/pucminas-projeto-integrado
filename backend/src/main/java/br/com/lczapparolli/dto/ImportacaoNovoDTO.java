package br.com.lczapparolli.dto;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
    @RestForm("layoutId")
    @PartType(TEXT_PLAIN)
    private Integer layoutId;

    /**
     * Nome do arquivo a ser importado
     */
    @RestForm("nomeArquivo")
    @PartType(TEXT_PLAIN)
    private String nomeArquivo;

    /**
     * Conteúdo do arquivo a ser importado
     */
    @RestForm("arquivo")
    @PartType(APPLICATION_OCTET_STREAM)
    private FileUpload arquivo;

}
