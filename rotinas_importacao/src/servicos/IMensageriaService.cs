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
  }
}