/**
 * Mapeamento das propriedades do layout para importação
 */
export default interface Layout {
  /**
   * Identificação única do layout
   */
  id: Number;

  /**
   * Descrição legível do layout
   */
  descricao: String;

  /**
   * Indicação se o layout está ativo ou não
   */
  ativo: Boolean;
}