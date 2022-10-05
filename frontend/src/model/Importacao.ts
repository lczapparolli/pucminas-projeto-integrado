import Layout from "./Layout";
import Situacao from "./Situacao";

/**
 * Modelo para os dados de uma importação
 */
export default interface Importacao {
  /**
   * Identificação única da importação
   */
  id: number;

  /**
   * Dados do layout relacionada à importação
   */
  layout: Layout;

  /**
   * Nome do arquivo importado
   */
  nomeArquivo: string;

  /**
   * Situação da importação
   */
  situacao: Situacao;

  /**
   * Data e hora em que a importação teve início
   */
  dataHoraInicio: Date;

  /**
   * Data e hora em que a importação foi concluída
   */
  dataHoraFim?: Date;

  /**
   * Percentual do arquivo que já foi processado
   */
  percCompleto?: number;
}