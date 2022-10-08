/**
 * Base da URL para execução dos serviços
 */
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

/**
 * URL completa para o consumo dos endpoints de layout
 */
export const LAYOUT_URL = `${API_BASE_URL}/layout`;

/**
 * URL completa para o consumo dos endpoints de importação
 */
export const IMPORTACAO_URL = `${API_BASE_URL}/importacao`;