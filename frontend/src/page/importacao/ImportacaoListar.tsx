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

/**
 * Página para listar as importações ativas
 * 
 * @returns Retorna o componente de página
 */
export default function ImportacaoListar() {
  const [importacoes, setImportacoes] = useState<Importacao[]>([]);

  const importacoesRef = useRef(importacoes);

  /**
   * Mantém a referência da lista de importações para permitir a atualização
   */
  useEffect(() => {
    importacoesRef.current = importacoes;
  }, [importacoes]);

  /**
   * Inicializa o consumo dos eventos de atualização
   */
  useEffect(() => {
    const eventSource = obterAtualizacoes(onSituacaoImportacao);

    // Evento para finalizar a conexão ao sair da página
    return () => { eventSource.close(); }
  }, []);

  /**
   * Processa a atualização na situação de uma importação
   * 
   * @param situacao Situação atualizada de uma importação
   */
  const onSituacaoImportacao = async (situacao: SituacaoImportacao) => {
    // Faz uma cópia simples da lista
    let importacoesCopy = importacoesRef.current.slice(0);
    
    let importacao = importacoesCopy.find(i => i.id === situacao.importacaoId);
    if (!importacao) {
      // Caso a importação não esteja na lista, consulta os dados no servidor
      importacao = await consultarImportacao(situacao.importacaoId);
      importacoesCopy.push(importacao);
    }

    // Atualiza os dados do processamento
    importacao.situacao = situacao.situacao;
    importacao.percCompleto = Math.trunc((situacao.posicao / situacao.tamanho) * 100);

    // Atualiza a página
    setImportacoes(importacoesCopy);
  }

  /**
   * Renderiza uma importação como uma linha da tabela
   * 
   * @param importacao Dados da importação a ser exibida
   * @returns Retorna a linha da tabela referente à importação
   */
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