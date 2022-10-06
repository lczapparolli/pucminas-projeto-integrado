using System;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
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
    private readonly string eventsQueueName;
    private readonly bool eventsQueueDurable;
    private readonly string eventsExchangeName;
    private readonly string commandsExchangeName;

    private readonly JsonSerializerSettings jsonSerializerSettings;

    /// <summary>
    /// Inicializa o serviço
    /// </summary>
    public RabbitMQService()
    {
      jsonSerializerSettings = new JsonSerializerSettings();
      jsonSerializerSettings.Converters.Add(new Newtonsoft.Json.Converters.StringEnumConverter());

      eventsQueueName = Environment.GetEnvironmentVariable("rabbitMQ_events_queue_name");
      eventsQueueDurable = bool.Parse(Environment.GetEnvironmentVariable("rabbitMQ_events_queue_durable"));
      eventsExchangeName = Environment.GetEnvironmentVariable("rabbitMQ_events_exchange_name");
      commandsExchangeName = Environment.GetEnvironmentVariable("rabbitMQ_commands_exchange_name");

      conexao = ObterConexao();
      modelo = conexao.CreateModel();

      modelo.QueueDeclare(
        queue: eventsQueueName,
        durable: eventsQueueDurable,
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
      var properties = modelo.CreateBasicProperties();
      properties.ContentType = "application/json";

      modelo.BasicPublish(
        exchange: eventsExchangeName,
        routingKey: eventsQueueName,
        basicProperties: properties,
        body: Encoding.UTF8.GetBytes(JsonConvert.SerializeObject(evento, jsonSerializerSettings)));
    }

    /// <summary>
    /// Inicia o recebimento de comandos
    /// </summary>
    /// <param name="identificacaoLayout">Identificação do layout</param>
    /// <param name="importacaoId">Identificação única da importação</param>
    /// <param name="handler">Método a ser chamado para quando um comando for recebido</param>
    public void ReceberComando(string identificacao, int importacaoId, EventHandler<ComandoImportacao> handler)
    {
      // Cria a fila e a exchange
      var commandsQueueName = $"{identificacao}-{importacaoId}";
      DeclararFilaComandos(commandsQueueName);

      var _this = this;
      var consumer = new EventingBasicConsumer(modelo);
      consumer.Received += (sender, eventArgs) =>
      {
        // Garante o processamento apenas da exchange correta
        if (eventArgs.Exchange.Equals(commandsExchangeName))
        {
          var texto = Encoding.UTF8.GetString(eventArgs.Body.ToArray());
          var mensagem = JsonConvert.DeserializeObject<ComandoImportacao>(texto);
          handler(_this, mensagem);
        }
      };

      // Inicia o consumo da fila
      modelo.BasicConsume(
        queue: commandsQueueName,
        autoAck: true,
        consumer: consumer
      );
    }

    /// <summary>
    /// Cria a exchange, a fila e o vínculo entre elas para o recebimento de comandos
    /// </summary>
    /// <param name="commandsQueueName">Nome da fila exclusiva da importação em questão</param>
    private void DeclararFilaComandos(string commandsQueueName)
    {
      modelo.ExchangeDeclare(
        exchange: commandsExchangeName,
        type: "topic",
        durable: true,
        autoDelete: false
      );

      modelo.QueueDeclare(
        queue: commandsQueueName,
        durable: true,
        exclusive: false,
        autoDelete: true,
        arguments: null);

      modelo.QueueBind(
        queue: commandsQueueName,
        exchange: commandsExchangeName,
        routingKey: commandsQueueName
      );
    }
  }
}