package br.com.lczapparolli.mock;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

import java.nio.charset.Charset;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * Implementação da interface {@link FileUpload} para ser utilizada nos testes
 *
 * @author lczapparolli
 */
@AllArgsConstructor
public class FileUploadMock implements FileUpload {

    private String name;
    private String fileName;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Path filePath() {
        return Path.of("tmp", "uploads", fileName);
    }

    @Override
    public String fileName() {
        return fileName;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public String contentType() {
        return APPLICATION_OCTET_STREAM;
    }

    @Override
    public String charSet() {
        return Charset.defaultCharset().toString();
    }
}
