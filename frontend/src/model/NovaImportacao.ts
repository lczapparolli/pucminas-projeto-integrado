/**
 * Mapeamento das propriedades necessárias para a criação de uma nova importação
 */
export default interface NovaImportacao {

  /**
   * Identificação do layout a ser utilizado
   */
  layoutId: number;

  /**
   * Arquivo a ser importado
   */
  arquivo: File;
}