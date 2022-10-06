import React, { useEffect, useState, useRef } from "react";

import Col  from "react-bootstrap/Col";
import Container  from "react-bootstrap/Container";
import ProgressBar  from "react-bootstrap/ProgressBar";
import Row  from "react-bootstrap/Row";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

import Layout from "../../layout/Layout";

import { cancelarImportacao, consultarImportacao, obterAtualizacoes } from "../../controller/ImportacaoController";

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
  const [modalVisivel, setModalVisivel] = useState<boolean>(false);
  const [importacaoCancelar, setImportacaoCancelar] = useState<number>(0);

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
   * Exibe o modal para que o usuário possa confirmar o cancelamento
   * 
   * @param importacaoId Identificação da importação a ser cancelada
   */
  const exibirModal = (importacaoId: number) => {
    setImportacaoCancelar(importacaoId);
    setModalVisivel(true);
  }

  /**
   * Oculta o modal de cancelamento e limpa a identificação da importação a ser cancelada
   */
  const ocultarModal = () => {
    setImportacaoCancelar(0);
    setModalVisivel(false);
  }

  /**
   * Confirma o cancelamento e oculta o modal
   */
  const confirmarCancelamento = () => {
    if (importacaoCancelar > 0) {
      cancelarImportacao(importacaoCancelar);
    }
    ocultarModal();
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
        <td>
          {
            (importacao.situacao === Situacao.PROCESSANDO) && <Button variant="danger" onClick={() => exibirModal(importacao.id)}>Cancelar</Button>
          }
        </td>
      </tr>
    );
  }

  return (
    <>
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
                    <th>Ação</th>
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

      <Modal show={modalVisivel} onHide={ocultarModal}>
        <Modal.Header>
          <Modal.Title>Tem certeza?</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <span>Tem certeza que deseja cancelar essa importação? O processamento será interrompido e não poderá ser retomado.</span>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={ocultarModal}>
            Continuar processamento
          </Button>
          <Button variant="primary" onClick={confirmarCancelamento}>
            Cancelar importação
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}