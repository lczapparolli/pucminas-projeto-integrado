import axios from "axios";
import { LAYOUT_URL } from "../../Parametros";
import { listarLayoutsAtivos } from "../LayoutController";

jest.mock("axios");
const mockedAxios = jest.mocked(axios, true);
const mockedLayouts = [
  { id: 1, descricao: "Teste 1", ativo: true },
  { id: 2, descricao: "Teste 2", ativo: true },
  { id: 3, descricao: "Teste 3", ativo: true }
];

describe("LayoutController", () => {
  describe("function listarLayoutsAtivos()", () => {
    
    test("deve retornar um promise com uma lista", async () => {
      mockedAxios.get.mockResolvedValueOnce({ data: [] });

      var resultado = listarLayoutsAtivos();
      expect(resultado).toBeInstanceOf(Promise);
      expect(await resultado).toBeInstanceOf(Array);
    });

    test("deve chamar o endpoint de consulta de layouts, filtrando layouts ativos", async () => {
      mockedAxios.get.mockResolvedValueOnce({ data: mockedLayouts });
      
      var resultado = await listarLayoutsAtivos();
      expect(axios.get).toHaveBeenCalledWith(LAYOUT_URL, { params: { ativo: true } });
      expect(resultado).toBe(mockedLayouts);
    });

  });
});