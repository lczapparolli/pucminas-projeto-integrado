namespace rotinas_importacao.dtos
{
  /// <summary>
  /// Dados do comando recebido
  /// </summary>
  public class ComandoImportacao
  {
    /// <summary>
    /// Comandos possíveis
    /// </summary>
    public enum Comando
    {
      CANCELAR
    }

    /// <summary>
    /// Comando recebido
    /// </summary>
    public Comando comando { get; set; }
  }
}