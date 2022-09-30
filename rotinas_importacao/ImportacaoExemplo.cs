using System;
using System.IO;
using Microsoft.AspNetCore.Http;
using Microsoft.Azure.WebJobs;
using Microsoft.Extensions.Logging;

namespace rotinas_importacao
{
    public class ImportacaoExemplo
    {

        [FunctionName("ImportacaoExemploBlob")]
        public void RunBlobTrigger([BlobTrigger("importacao-exemplo/{nomeArquivo}")]TextReader arquivo, string nomeArquivo, ILogger log)
        {
            ProcessarImportacao(arquivo, nomeArquivo, log);
        }

        [FunctionName("ImportacaoExemploHttp")]
        public void RunHttpTrigger(
            [HttpTrigger("post", Route = "importacao-exemplo/{nomeArquivo}")] HttpRequest req,
            [Blob("importacao-exemplo/{nomeArquivo}")] TextReader arquivo,
            string nomeArquivo,
            ILogger log)
        {
            ProcessarImportacao(arquivo, nomeArquivo, log);
        }

        private void ProcessarImportacao(TextReader arquivo, string nomeArquivo, ILogger log)
        {
            log.LogInformation($"Iniciando importação do arquivo '{nomeArquivo}'");
            String linha = arquivo.ReadLine();
            while (linha != null) {
                log.LogInformation($"Linha processada: '{linha}'");
                
                // Simulando carga de trabalho
                System.Threading.Thread.Sleep(1000);
                linha = arquivo.ReadLine();
            }
            log.LogInformation($"Processamento do arquivo '{nomeArquivo}' concluído");
        }
    }
}
