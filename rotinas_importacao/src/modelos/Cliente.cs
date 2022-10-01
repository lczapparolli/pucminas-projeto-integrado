using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  /// <summary>
  /// Classe para armazenamento dos dados de um cliente
  /// </summary>
  [Table("cliente")]
  public class Cliente
  {
    /// <summary>
    /// Número do documento do cliente. Chave primária da tabela
    /// </summary>
    [Key]
    [Column("documento")]
    public long Documento { get; set; }
    
    /// <summary>
    /// Nome do cliente
    /// </summary>
    [Column("nome")]
    public string Nome { get; set; }
    
    /// <summary>
    /// Data de nascimento do cliente
    /// </summary>
    [Column("data_nascimento")]
    public DateTime DataNascimento { get; set; }
    
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

    /// <summary>
    /// Lista de endereços vinculados ao cliente
    /// </summary>
    public ICollection<Endereco> Enderecos { get; set; }

    /// <summary>
    /// Lista de telefones vinculados ao cliente
    /// </summary>
    public ICollection<Telefone> Telefones { get; set; }
  }
}