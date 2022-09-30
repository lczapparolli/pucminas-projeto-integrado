using Microsoft.EntityFrameworkCore;

namespace rotinas_importacao.modelos
{
  public class CrmContext : DbContext
  {
    public CrmContext(DbContextOptions<CrmContext> options) : base(options)
    {
    }

    public DbSet<Cliente> Clientes { get; set; }
  }
}