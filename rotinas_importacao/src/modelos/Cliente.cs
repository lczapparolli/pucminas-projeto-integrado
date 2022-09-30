using System;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  [Table("cliente")]
  public class Cliente
  {
    [Column("cliente_id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long clienteId { get; set; }
    [Column("nome")]
    public string nome { get; set; }
    [Column("data_nascimento")]
    public DateTime dataNascimento { get; set; }
    [Column("data_hora_criacao")]
    public DateTime dataHoraCriacao { get; set; }
    [Column("data_hora_atualizacao")]
    public DateTime? dataHoraAtualizacao { get; set; }
  }
}