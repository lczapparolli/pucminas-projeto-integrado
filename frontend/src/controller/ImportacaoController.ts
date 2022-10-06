import axios from "axios";
import Importacao from "../model/Importacao";
import NovaImportacao from "../model/NovaImportacao";
import SituacaoImportacao from "../model/SituacaoImportacao";
import { IMPORTACAO_URL } from "../Parametros";

/**
 * Envia um arquivo juntamente com o layout para ser importado pelo servidor
 * 
 * @param importacao Dados do layout e do arquivo a serem importados
 */
export async function criarImportacao(importacao: NovaImportacao) {
  if (importacao.layoutId <= 0) {
    throw new Error("Id do layout não informado");
  }

  // Monta os dados da requisição
  const dados = new FormData();
  dados.append("layoutId", importacao.layoutId.toString());
  dados.append("arquivo", importacao.arquivo!);
  dados.append("nomeArquivo", importacao.arquivo.name);

  await axios.post(IMPORTACAO_URL, dados, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
}

/**
 * Consulta uma importação
 * 
 * @param importacaoId Identificação da importação a ser consultada
 * @returns Retorna a importação encontrada
 */
export async function consultarImportacao(importacaoId: number): Promise<Importacao> {
  var resultado = await axios.get<Importacao>(`${IMPORTACAO_URL}/${importacaoId}`,
    {
      headers: { "Accept": "application/json" }
    });

  return resultado.data;
}

/**
 * Realiza a consulta de atualizações das importações de forma contínua
 * 
 * @param proc Função a ser processada quando houver um novo evento for recebido
 * @returns Retorna a referência ao evento para cancelamento
 */
export function obterAtualizacoes(proc: (data: SituacaoImportacao) => void): EventSource {
  var eventSource = new EventSource(`${IMPORTACAO_URL}/situacao`, { withCredentials: false });
  
  eventSource.onmessage = (message) => {
    proc(JSON.parse(message.data))
  }

  return eventSource;
}

/**
 * Realiza o cancelamento de uma importação. Interrompendo o processamento
 * 
 * @param importacaoId Identificação da importação a ser cancelada
 */
export async function cancelarImportacao(importacaoId: number) {
  await axios.post(`${IMPORTACAO_URL}/${importacaoId}/cancelar`);
}