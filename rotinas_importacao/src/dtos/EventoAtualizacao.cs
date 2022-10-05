namespace rotinas_importacao.dtos
{
  /// <summary>
  /// Classe para armazenar as atualizações do processamento dos arquivos
  /// </summary>
  public class EventoAtualizacao
  {
    /// <summary>
    /// Situações do processamento
    /// </summary>
    public enum Situacao
    {
      PROCESSANDO, PAUSADA, CANCELADA, SUCESSO, ERRO
    }

    /// <summary>
    /// Identificação da importação
    /// </summary>
    public int importacaoId { get; set; }

    /// <summary>
    /// Tamanho do arquivo em bytes
    /// </summary>
    public long tamanho { get; set; }

    /// <summary>
    /// Posição estimada da leitura do arquivo
    /// </summary>
    public long posicao { get; set; }

    /// <summary>
    /// Situação do processamento
    /// </summary>
    public Situacao situacao { get; set; }
  }
}