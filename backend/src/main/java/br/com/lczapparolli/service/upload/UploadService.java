package br.com.lczapparolli.service.upload;

import br.com.lczapparolli.erro.ResultadoOperacao;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * Interface para os serviços de carregamento de arquivos para os serviços de nuvem
 *
 * @author lczapparolli
 */
public interface UploadService {

    /**
     * Realiza o carregamento do arquivo informado
     *
     * @param identificacaoLayout Identificação do layout para separação no serviço
     * @param nomeArquivo Nome do arquivo a ser apresentado
     * @param arquivo Conteúdo do arquivo a ser carregado
     * @return Retorna o resultado da operação com a identificação dos erros, caso haja algum
     */
    ResultadoOperacao<Void> carregarArquivo(String identificacaoLayout, String nomeArquivo, FileUpload arquivo);

}
