import axios from "axios";
import NovaImportacao from "../model/NovaImportacao";
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