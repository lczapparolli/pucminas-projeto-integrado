package br.com.lczapparolli.service.upload;

import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_UPLOAD_CONECTAR;
import static br.com.lczapparolli.erro.ErroAplicacao.ERRO_UPLOAD_TRANSMISSAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import javax.enterprise.context.ApplicationScoped;

import br.com.lczapparolli.erro.ResultadoOperacao;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementação da interface de upload para o Azure
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class AzureUploadService implements UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureUploadService.class);

    /**
     * Configuração da conta para upload dos arquivos
     */
    @ConfigProperty(name = "upload.azure.account")
    String storageAccount;

    /**
     * Envia o arquivo informado para o Blob storage da Azure
     *
     * @param identificacaoLayout Identificação do layout para separação no serviço
     * @param nomeArquivo Nome do arquivo a ser apresentado
     * @param arquivo Conteúdo do arquivo a ser carregado
     * @return Retorna os possíveis erros encontrados ao carregar o arquivo
     */
    @Override
    public ResultadoOperacao<Void> carregarArquivo(String identificacaoLayout, String nomeArquivo, FileUpload arquivo) {
        var resultadoOperacao = new ResultadoOperacao<Void>();

        try {
            var fileInputStream = new FileInputStream(new File(arquivo.uploadedFile().toUri()));

            var storageAccount = CloudStorageAccount.parse(this.storageAccount);
            var blobClient = storageAccount.createCloudBlobClient();
            var container = blobClient.getContainerReference(identificacaoLayout);
            container.createIfNotExists();

            var blob = container.getBlockBlobReference(nomeArquivo);
            blob.upload(fileInputStream, arquivo.size());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Arquivo carregado: {} - Tamanho: {}", nomeArquivo, arquivo.size());
            }
        } catch (URISyntaxException | InvalidKeyException | StorageException e) {
            resultadoOperacao.addErro(ERRO_UPLOAD_CONECTAR);
            LOGGER.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            resultadoOperacao.addErro(ERRO_UPLOAD_ARQUIVO_NAO_ENCONTRADO);
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            resultadoOperacao.addErro(ERRO_UPLOAD_TRANSMISSAO);
            LOGGER.error(e.getMessage(), e);
        }

        return resultadoOperacao;
    }
}