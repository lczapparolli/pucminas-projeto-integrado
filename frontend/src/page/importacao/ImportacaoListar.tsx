import React, { useEffect, useState, useRef } from "react";

import Col  from "react-bootstrap/Col";
import Container  from "react-bootstrap/Container";
import ProgressBar  from "react-bootstrap/ProgressBar";
import Row  from "react-bootstrap/Row";
import Table from "react-bootstrap/Table";

import Layout from "../../layout/Layout";

import { consultarImportacao, obterAtualizacoes } from "../../controller/ImportacaoController";

import Importacao from "../../model/Importacao";
import SituacaoImportacao from "../../model/SituacaoImportacao";
import Situacao from "../../model/Situacao";

export default function ImportacaoListar() {
  const [importacoes, setImportacoes] = useState<Importacao[]>([]);

  const importacoesRef = useRef(importacoes);

  const onSituacaoImportacao = async (situacao: SituacaoImportacao) => {
    let importacoesCopy = importacoesRef.current.slice(0);
    
    let importacao = importacoesCopy.find(i => i.id === situacao.importacaoId);
    if (!importacao) {
      importacao = await consultarImportacao(situacao.importacaoId);
      importacoesCopy.push(importacao);
    }

    importacao.situacao = situacao.situacao;
    importacao.percCompleto = Math.trunc((situacao.posicao / situacao.tamanho) * 100);
    setImportacoes(importacoesCopy);
  }

  useEffect(() => {
    importacoesRef.current = importacoes;
  }, [importacoes]);

  useEffect(() => {
    const eventSource = obterAtualizacoes(onSituacaoImportacao);
    return () => { eventSource.close(); }
  }, []);

  const exibirImportacao = (importacao: Importacao) => {
    let progressVariant: string;

    switch (importacao.situacao) {
      case Situacao.SUCESSO:
        progressVariant = "success";
        break;
      case Situacao.ERRO:
        progressVariant = "danger";
        break;
      case Situacao.CANCELADA:
      case Situacao.PAUSADA:
        progressVariant = "warning";
        break;
      default:
        progressVariant = "info";
        break;
    }

    return (
      <tr key={importacao.id}>
        <td>{ importacao.id }</td>
        <td>{ importacao.layout.descricao }</td>
        <td>{ importacao.nomeArquivo }</td>
        <td>{ importacao.situacao }</td>
        <td><ProgressBar now={importacao.percCompleto} label={`${importacao.percCompleto}%`} variant={progressVariant} /></td>
      </tr>
    );
  }

  return (
    <Layout>
      <Container>
        <Row className="mb-3">
          <Col>
            <h1>Importações em progresso</h1>
          </Col>
        </Row>
        <Row>
          <Col>
            <Table striped>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Layout</th>
                  <th>Arquivo</th>
                  <th>Situação</th>
                  <th>Progresso</th>
                </tr>
              </thead>
              <tbody>
                { importacoes.map(importacao => exibirImportacao(importacao)) }
              </tbody>
            </Table>
          </Col>
        </Row>
      </Container>
    </Layout>
  );
}