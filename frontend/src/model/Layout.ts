/**
 * Mapeamento das propriedades do layout para importação
 */
export default interface Layout {
  /**
   * Identificação única do layout
   */
  id: number;

  /**
   * Descrição legível do layout
   */
  descricao: string;

  /**
   * Indicação se o layout está ativo ou não
   */
  ativo: boolean;
}