using System;
using rotinas_importacao.dtos;

namespace rotinas_importacao.servicos
{
  /// <summary>
  /// Interface para os serviços de mensageria
  /// </summary>
  public interface IMensageriaService
  {

    /// <summary>
    /// Envia ao serviço de mensageria 
    /// </summary>
    /// <param name="evento">Objeto com os dados da situação da importação</param>
    public void EnviarAtualizacao(EventoAtualizacao evento);
    
    /// <summary>
    /// Inicia o recebimento de comandos
    /// </summary>
    /// <param name="identificacaoLayout">Identificação do layout</param>
    /// <param name="importacaoId">Identificação única da importação</param>
    /// <param name="handler">Método a ser chamado para quando um comando for recebido</param>
    public void ReceberComando(string identificacaoLayout, int importacaoId, EventHandler<ComandoImportacao> handler);
  }
}