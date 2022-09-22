package br.com.lczapparolli.dto;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
    @Schema(name = "layoutId", description = "Identificação do layout a ser utilizado para a importação", example = "1", required = true)
    private Integer layoutId;

    /**
     * Nome do arquivo que será apresentado para identificação
     */
    @RestForm("nomeArquivo")
    @PartType(TEXT_PLAIN)
    @Schema(name = "nomeArquivo", description = "Nome do arquivo que será apresentado para identificação", example = "arquivo_importado_1.txt", required = true)
    private String nomeArquivo;

    /**
     * Conteúdo do arquivo a ser importado
     */
    @RestForm("arquivo")
    @PartType(APPLICATION_OCTET_STREAM)
    @Schema(name = "arquivo", description = "Conteúdo do arquivo a ser importado", implementation = InputStream.class, required = true)
    private FileUpload arquivo;

}
