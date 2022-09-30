using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  [Table("telefone")]
  public class Telefone
  {
    [Key]
    [Column("telefone_id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long TelefoneId { get; set; }

    [Column("documento_cliente")]
    [ForeignKey("Cliente")]
    public long DocumentoCliente { get; set; }
    public Cliente Cliente { get; set; }
  
    [Column("ddd")]
    public int DDD { get; set; }
  
    [Column("telefone")]
    public int Numero { get; set; }
  
    [Column("ramal")]
    public int? Ramal { get; set; }
  
    [Column("data_hora_criacao")]
    public DateTime DataHoraCriacao { get; set; }

    [Column("data_hora_atualizacao")]
    public DateTime? DataHoraAtualizacao { get; set; }


  }
}