using System;
using Microsoft.Azure.Functions.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using rotinas_importacao.modelos;

[assembly: FunctionsStartup(typeof(rotinas_importacao.StartUp))]

namespace rotinas_importacao
{
  public class StartUp : FunctionsStartup
  {
    /// <summary>
    /// Realiza as configurações necessárias para a execução do serviço
    /// </summary>
    /// <param name="builder"></param>
    public override void Configure(IFunctionsHostBuilder builder)
    {
      var connectionString = Environment.GetEnvironmentVariable("crmDatabaseConnection");
      var serverVersion = ServerVersion.AutoDetect(connectionString);
      
      builder.Services.AddDbContext<CrmContext>(options => options.UseMySql(connectionString, serverVersion));
      builder.Services.AddLogging();
    }
  }
}