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

export default function ImportacaoNovo() {
  const [layouts, setLayouts] = useState<LayoutModel[]>([]);
  const [layoutId, setLayoutId] = useState<number>();
  const [erros, setErros] = useState({ layout: "", arquivo: "" });
  const [arquivo, setArquivo] = useState<File>();

  const navigate = useNavigate();

  useEffect(() => {
    listarLayoutsAtivos().then(resultado => setLayouts(resultado));
  }, []);

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

  const formSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!validarInputs()) {
      return;
    }

    await criarImportacao({
      layoutId: layoutId ?? 0,
      arquivo: arquivo!
    });

    navigate("/importacao/listar");
  }

  const layoutChange = (event: React.FormEvent<HTMLSelectElement>) => {
    const id = Number.parseFloat(event.currentTarget.value);
    setLayoutId(id);
  }

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