using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace rotinas_importacao.modelos
{
  /// <summary>
  /// Classe para o armazenamento dos dados de um endereço de cliente
  /// </summary>
  [Table("endereco")]
  public class Endereco
  {

    /// <summary>
    /// Identificação única do endereço. Chave primária da tabela, gerada automaticamente
    /// </summary>
    [Key]
    [Column("endereco_id")]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public long EnderecoId { get; set; }

    /// <summary>
    /// Número do documento do cliente vinculado ao endereço
    /// </summary>
    [Column("documento_cliente")]
    [ForeignKey("Cliente")]
    public long DocumentoCliente { get; set; }
    /// <summary>
    /// Referência ao cliente vinculado ao endereço
    /// </summary>
    public Cliente Cliente { get; set; }

    /// <summary>
    /// Identificação do logradouro
    /// </summary>
    [Column("logradouro")]
    public string Logradouro { get; set; }

    /// <summary>
    /// Número da residência
    /// </summary>
    [Column("numero")]
    public string Numero { get; set; }

    /// <summary>
    /// Complemento do endereço
    /// </summary>
    [Column("complemento")]
    public string Complemento { get; set; }

    /// <summary>
    /// Bairro do endereço
    /// </summary>
    [Column("bairro")]
    public string Bairro { get; set; }

    /// <summary>
    /// Município do endereço
    /// </summary>
    [Column("municipio")]
    public string Municipio { get; set; }

    /// <summary>
    /// UF ao qual o município pertence
    /// </summary>
    [Column("uf")]
    public string UF { get; set; }

    /// <summary>
    /// CEP da residência
    /// </summary>
    [Column("cep")]
    public int CEP { get; set; }

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