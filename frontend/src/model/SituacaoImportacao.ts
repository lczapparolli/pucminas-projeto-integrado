import Situacao from "./Situacao";

/**
 * Modelo para os dados de atualização da situação de uma importação
 */
export default interface SituacaoImportacao {

  /**
   * Identificação única da importação
   */
  importacaoId: number;

  /**
   * Tamanho do arquivo em bytes
   */
  tamanho: number;

  /**
   * Posição aproximada do processamento (em bytes)
   */
  posicao: number;

  /**
   * Situação do processamento
   */
  situacao: Situacao;
}