using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  [Table("cliente")]
  public class Cliente
  {
    [Key]
    [Column("documento")]
    public long Documento { get; set; }
    
    [Column("nome")]
    public string Nome { get; set; }
    
    [Column("data_nascimento")]
    public DateTime DataNascimento { get; set; }
    
    [Column("data_hora_criacao")]
    public DateTime DataHoraCriacao { get; set; }
    
    [Column("data_hora_atualizacao")]
    public DateTime? DataHoraAtualizacao { get; set; }

    public ICollection<Endereco> Enderecos { get; set; }

    public ICollection<Telefone> Telefones { get; set; }
  }
}