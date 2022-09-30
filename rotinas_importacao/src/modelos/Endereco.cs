using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  [Table("endereco")]
  public class Endereco
  {

    [Key]
    [Column("endereco_id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long EnderecoId { get; set; }

    [Column("documento_cliente")]
    [ForeignKey("Cliente")]
    public long DocumentoCliente { get; set; }
    public Cliente Cliente { get; set; }

    [Column("logradouro")]
    public string Logradouro { get; set; }

    [Column("numero")]
    public string Numero { get; set; }

    [Column("complemento")]
    public string Complemento { get; set; }

    [Column("bairro")]
    public string Bairro { get; set; }

    [Column("municipio")]
    public string Municipio { get; set; }

    [Column("uf")]
    public string UF { get; set; }

    [Column("cep")]
    public int CEP { get; set; }

    [Column("data_hora_criacao")]
    public DateTime DataHoraCriacao { get; set; }

    [Column("data_hora_atualizacao")]
    public DateTime? DataHoraAtualizacao { get; set; }

  }
}