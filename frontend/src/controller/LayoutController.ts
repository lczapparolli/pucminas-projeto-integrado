import axios from "axios";
import Layout from "../model/Layout";
import { BASE_URL, LAYOUT_URL } from "../Parametros";

/**
 * Consulta os layouts disponíveis para a importação
 * 
 * @returns Retorna um Promise que resolve para uma lista de layouts
 */
export async function listarLayoutsAtivos() : Promise<Array<Layout>> {
  let resultado = await axios.get(LAYOUT_URL, {
    params: {
      ativo: true
    }
  });

  return resultado.data;
}