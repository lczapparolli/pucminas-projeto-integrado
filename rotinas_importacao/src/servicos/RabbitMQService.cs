using System;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;
using rotinas_importacao.dtos;

namespace rotinas_importacao.servicos
{
  /// <summary>
  /// Serviço de envio de mensagens para o RabbitMQ
  /// </summary>
  public class RabbitMQService : IMensageriaService, IDisposable
  {
    private readonly IModel modelo;
    private readonly IConnection conexao;
    private readonly string queueName;
    private readonly bool queueDurable;
    private readonly string exchangeName;

    /// <summary>
    /// Inicializa o serviço
    /// </summary>
    public RabbitMQService()
    {
      queueName = Environment.GetEnvironmentVariable("rabbitMQ_queue_name");
      queueDurable = bool.Parse(Environment.GetEnvironmentVariable("rabbitMQ_queue_durable"));
      exchangeName = Environment.GetEnvironmentVariable("rabbitMQ_exchange_name");
      
      conexao = ObterConexao();
      modelo = conexao.CreateModel();
      
      modelo.QueueDeclare(
        queue: queueName,
        durable: queueDurable,
        exclusive: false,
        autoDelete: false,
        arguments: null);
    }

    /// <summary>
    /// Obtém uma instância de conexão com o servidor
    /// </summary>
    /// <returns>Retorna a instância criada</returns>
    private IConnection ObterConexao()
    {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.HostName = Environment.GetEnvironmentVariable("rabbitMQ_hostName");
      connectionFactory.Port = int.Parse(Environment.GetEnvironmentVariable("rabbitMQ_port"));
      connectionFactory.UserName = Environment.GetEnvironmentVariable("rabbitMQ_userName");
      connectionFactory.Password = Environment.GetEnvironmentVariable("rabbitMQ_password");
      return connectionFactory.CreateConnection();
    }

    /// <summary>
    /// Encerra a conexão com o servidor
    /// </summary>
    public void Dispose()
    {
      modelo.Close();
      conexao.Close();
    }

    /// <summary>
    /// Envia ao serviço de mensageria 
    /// </summary>
    /// <param name="evento">Objeto com os dados da situação da importação</param>
    public void EnviarAtualizacao(EventoAtualizacao evento)
    {
      modelo.BasicPublish(
        exchange: exchangeName,
        routingKey: queueName,
        basicProperties: null,
        body: Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(evento)));
    }
  }
}