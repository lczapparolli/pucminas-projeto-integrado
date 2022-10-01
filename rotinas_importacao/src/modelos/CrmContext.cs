using Microsoft.EntityFrameworkCore;

namespace rotinas_importacao.modelos
{
  /// <summary>
  /// Contexto de banco de dados do CRM
  /// </summary>
  public class CrmContext : DbContext
  {
    public CrmContext(DbContextOptions<CrmContext> options) : base(options)
    {
    }

    /// <summary>
    /// Acesso aos métodos de manipulação de clientes
    /// </summary>
    public DbSet<Cliente> Clientes { get; set; }

    /// <summary>
    /// Acesso aos métodos de manipulação de endereços
    /// </summary>
    public DbSet<Endereco> Enderecos { get; set; }

    /// <summary>
    /// Acesso aos métodos de manipulação de telefones
    /// </summary>
    public DbSet<Telefone> Telefones { get; set; }
  }
}