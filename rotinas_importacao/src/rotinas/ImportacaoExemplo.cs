using System;
using System.Globalization;
using System.IO;
using Microsoft.AspNetCore.Http;
using Microsoft.Azure.WebJobs;
using Microsoft.Extensions.Logging;
using rotinas_importacao.modelos;
using System.Linq;

namespace rotinas_importacao.rotinas
{
  public class ImportacaoExemplo
  {

    private readonly CrmContext contexto;

    public ImportacaoExemplo(CrmContext contexto)
    {
      this.contexto = contexto;
    }

    [FunctionName("ImportacaoExemploBlob")]
    public void RunBlobTrigger([BlobTrigger("importacao-exemplo/{nomeArquivo}")] Stream arquivo, string nomeArquivo, ILogger log)
    {
      ProcessarImportacao(arquivo, nomeArquivo, log);
    }

    [FunctionName("ImportacaoExemploHttp")]
    public void RunHttpTrigger(
        [HttpTrigger("post", Route = "importacao-exemplo/{nomeArquivo}")] HttpRequest req,
        [Blob("importacao-exemplo/{nomeArquivo}", FileAccess.Read)] Stream arquivo,
        string nomeArquivo,
        ILogger log)
    {
      ProcessarImportacao(arquivo, nomeArquivo, log);
    }

    private void ProcessarImportacao(Stream arquivo, string nomeArquivo, ILogger log)
    {
      log.LogInformation($"Iniciando importação do arquivo '{nomeArquivo}'");
      using (var reader = new StreamReader(arquivo))
      {
        while (!reader.EndOfStream)
        {
          var linha = reader.ReadLine();
          ProcessarLinha(linha);
          log.LogInformation($"Linha processada: '{linha}'");
        }
      }
      log.LogInformation($"Processamento do arquivo '{nomeArquivo}' concluído");
    }

    private void ProcessarLinha(string linha)
    {
      var valores = linha.Split(";");
      if (valores[0] == "cliente")
      {
        ProcessarCliente(valores);
      }
      else if (valores[0] == "endereco")
      {
        ProcessarEndereco(valores);
      }
      else if (valores[0] == "telefone")
      {
        ProcessarTelefone(valores);
      }
    }

    private void ProcessarCliente(string[] valores)
    {
      var documento = long.Parse(valores[1]);
      var cliente = contexto.Clientes.Find(documento);
      if (cliente == null)
      {
        cliente = new Cliente
        {
          Documento = documento,
          DataHoraCriacao = DateTime.Now
        };
        contexto.Clientes.Add(cliente);
      }
      else
      {
        cliente.DataHoraAtualizacao = DateTime.Now;
      }
      cliente.Nome = valores[2].Trim();
      cliente.DataNascimento = DateTime.ParseExact(valores[3], "yyyy-MM-dd", CultureInfo.InvariantCulture);

      contexto.SaveChanges();
    }

    private void ProcessarEndereco(string[] valores)
    {
      var documento = long.Parse(valores[1]);
      var logradouro = valores[2].Trim();
      var numero = valores[3].Trim();
      var complemento = valores[4].Trim();
      var bairro = valores[5].Trim();
      var municipio = valores[6].Trim();
      var uf = valores[7].Trim();
      var cep = int.Parse(valores[8].Trim().Replace("-", ""));

      var endereco = contexto.Enderecos
        .Where(e => e.DocumentoCliente == documento 
          && e.Logradouro == logradouro
          && e.Numero == numero
          && e.Complemento == complemento
          && e.Bairro == bairro
          && e.Municipio == municipio
          && e.UF == uf
          && e.CEP == cep)
        .FirstOrDefault();

      if (endereco == null)
      {
        endereco = new Endereco
        {
          DataHoraCriacao = DateTime.Now
        };
        contexto.Enderecos.Add(endereco);
      }
      else
      {
        endereco.DataHoraAtualizacao = DateTime.Now;
      }

      endereco.DocumentoCliente = documento;
      endereco.Logradouro = logradouro;
      endereco.Numero = numero;
      endereco.Complemento = complemento;
      endereco.Bairro = bairro;
      endereco.Municipio = municipio;
      endereco.UF = uf;
      endereco.CEP = cep;

      contexto.SaveChanges();
    }

    private void ProcessarTelefone(string[] valores)
    {
      var documento = long.Parse(valores[1]);
      var numTelefone = valores[2].Trim();
      var ramal = String.IsNullOrWhiteSpace(valores[3]) ? (int?) null : int.Parse(valores[3].Trim());

      var ddd = int.Parse(numTelefone.Substring(1, 2));
      var numero = int.Parse(numTelefone.Substring(5).Replace("-", ""));

      var telefone = contexto.Telefones
        .Where(t => t.DocumentoCliente == documento
          && t.DDD == ddd
          && t.Numero == numero
          && t.Ramal == ramal)
        .FirstOrDefault();

      if (telefone == null)
      {
        telefone = new Telefone
        {
          DataHoraCriacao = DateTime.Now
        };
        contexto.Telefones.Add(telefone);
      }
      else
      {
        telefone.DataHoraAtualizacao = DateTime.Now;
      }

      telefone.DocumentoCliente = documento;
      telefone.DDD = ddd;
      telefone.Numero = numero;
      telefone.Ramal = ramal;

      contexto.SaveChanges();
    }
  }
}
