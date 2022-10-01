using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  /// <summary>
  /// Classe para armazenamento dos dados de um telefone de cliente
  /// </summary>
  [Table("telefone")]
  public class Telefone
  {

    /// <summary>
    /// Identificação única do telefone. Chave primária da tabela, gerada automaticamente
    /// </summary>
    [Key]
    [Column("telefone_id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long TelefoneId { get; set; }

    /// <summary>
    /// Número do documento do cliente vinculado ao telefone
    /// </summary>
    [Column("documento_cliente")]
    [ForeignKey("Cliente")]
    public long DocumentoCliente { get; set; }
    /// <summary>
    /// Referência ao cliente vinculado ao telefone
    /// </summary>
    public Cliente Cliente { get; set; }

    /// <summary>
    /// Código de Discagen Direta a Distância
    /// </summary>
    [Column("ddd")]
    public int DDD { get; set; }

    /// <summary>
    /// Número do telefone do cliente (sem o DDD)
    /// </summary>
    [Column("telefone")]
    public int Numero { get; set; }

    /// <summary>
    /// Opcional. Ramal interno do telefone
    /// </summary>
    [Column("ramal")]
    public int? Ramal { get; set; }

    /// <summary>
    /// Data e hora em que o registro foi criado
    /// </summary>
    [Column("data_hora_criacao")]
    public DateTime DataHoraCriacao { get; set; }

    /// <summary>
    /// Data e hora da última atualização do registro
    /// </summary>
    [Column("data_hora_atualizacao")]
    public DateTime? DataHoraAtualizacao { get; set; }

  }
}