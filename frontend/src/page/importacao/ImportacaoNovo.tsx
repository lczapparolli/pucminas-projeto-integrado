import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import Col  from "react-bootstrap/Col";
import Container  from "react-bootstrap/Container";
import Row  from "react-bootstrap/Row";
import Form  from "react-bootstrap/Form";
import Button  from "react-bootstrap/Button";
import InputGroup from "react-bootstrap/InputGroup";

import Layout from "../../layout/Layout";

import { criarImportacao } from "../../controller/ImportacaoController";
import { listarLayoutsAtivos } from "../../controller/LayoutController";

import LayoutModel from "../../model/Layout";

/**
 * Página para carregamento de um arquivo e início de uma importação
 * 
 * @returns Retorna o componente da página
 */
export default function ImportacaoNovo() {
  const [layouts, setLayouts] = useState<LayoutModel[]>([]);
  const [layoutId, setLayoutId] = useState<number>();
  const [erros, setErros] = useState({ layout: "", arquivo: "" });
  const [arquivo, setArquivo] = useState<File>();

  const navigate = useNavigate();

  /**
   * Obtém a lista de layouts disponíveis
   */
  useEffect(() => {
    listarLayoutsAtivos().then(resultado => setLayouts(resultado));
  }, []);

  /**
   * Valida os dados fornecidos para os campos do formulário
   * 
   * @returns Retorna `true` caso os campos estejam corretos, `false` caso haja algum erro
   */
  const validarInputs = (): boolean => {
    const novosErros = { layout: "", arquivo: "" };

    if ((layoutId ?? 0) <= 0) {
      novosErros.layout = "Escolha um layout";
    }

    if (!arquivo) {
      novosErros.arquivo = "Escolha um arquivo";
    }

    setErros(novosErros);
    return (!novosErros.layout && !novosErros.arquivo);
  }

  /**
   * Processa a submissão do form, enviando os dados para o servidor
   * 
   * @param event Objeto contendo os dados do evento
   * @returns Retorna um Promise para quando o método é completado
   */
  const formSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!validarInputs()) {
      return;
    }

    // Envia os dados para o servidor
    await criarImportacao({
      layoutId: layoutId ?? 0,
      arquivo: arquivo!
    });

    // Redireciona para a página de acompanhamento
    navigate("/importacao/listar");
  }

  /**
   * Trata a alteração no campo de layout
   * 
   * @param event Objeto contendo os dados do evento
   */
  const layoutChange = (event: React.FormEvent<HTMLSelectElement>) => {
    const id = Number.parseFloat(event.currentTarget.value);
    setLayoutId(id);
  }

  /**
   * Trata a alteração no campo de arquivo
   * 
   * @param event Objeto contendo os dados do evento
   */
  const arquivoChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setArquivo(event.currentTarget.files?.item(0) ?? undefined);
  }

  return (
    <Layout>
      <Container fluid>
        <form onSubmit={formSubmit}>
          <Row className="mb-3">
            <Col>
              <h1>Nova importação</h1>
            </Col>
          </Row>
          <Row className="mb-3">
            <Form.Group as={Col}>
              <Form.Label htmlFor="inputLayout">Layout</Form.Label>
              <InputGroup hasValidation>
                <Form.Select id="inputLayout" name="layout" onChange={layoutChange} isInvalid={!!erros.layout} >
                  <option>Selecione um layout</option>
                  {
                    layouts.map(item => <option key={item.id} value={item.id}>{item.descricao}</option>)
                  }
                </Form.Select>
                <Form.Control.Feedback type="invalid">{erros.layout}</Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
            <Form.Group as={Col}>
              <Form.Label htmlFor="inputArquivo">Arquivo</Form.Label>
              <InputGroup hasValidation>
                <Form.Control id="inputArquivo" name="arquivo" type="file" onChange={arquivoChange} isInvalid={!!erros.arquivo} />
                <Form.Control.Feedback type="invalid">{erros.arquivo}</Form.Control.Feedback>
              </InputGroup>
            </Form.Group>
          </Row>
          <Row className="mb-3">
            <Form.Group as={Col} >
              <Button type="submit">Importar</Button>
            </Form.Group>
          </Row>
        </form>
      </Container>
    </Layout>
  );
}