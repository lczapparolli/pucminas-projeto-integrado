import axios from "axios";
import NovaImportacao from "../../model/NovaImportacao";
import { IMPORTACAO_URL } from "../../Parametros";
import { criarImportacao } from "../ImportacaoController";

jest.mock("axios");
const mockedAxios = jest.mocked(axios, true);

describe("ImportacaoController", () => {
  describe("criarImportacao", () => {
    test("deve chamar a API de criação de importação passando os dados recebidos", async () => {
      const dadosImportacao: NovaImportacao = {
        layoutId: 1,
        arquivo: new File([], "arquivo_teste.txt")
      };
      await criarImportacao(dadosImportacao);

      const dados = new FormData();
      dados.append("layoutId", "1");
      dados.append("arquivo", dadosImportacao.arquivo);
      dados.append("nomeArquivo", "arquivo_teste.txt");
    
      expect(axios.post).toHaveBeenCalledWith(IMPORTACAO_URL, dados, { headers: { "Content-Type": "multipart/form-data" } });
    });

    test("deve validar se foi informado um Id de layout válido", () => {
      const dadosImportacao: NovaImportacao = {
        layoutId: 1,
        arquivo: new File([], "arquivo_teste.txt")
      };

      dadosImportacao.layoutId = 0;
      expect(() => criarImportacao(dadosImportacao)).rejects.toThrowError("Id do layout não informado");

      dadosImportacao.layoutId = -1;
      expect(() => criarImportacao(dadosImportacao)).rejects.toThrowError("Id do layout não informado");
    });
  })
});