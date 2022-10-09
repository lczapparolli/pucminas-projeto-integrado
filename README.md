# Importação distribuída de arquivos
Desenvolvido para a disciplina de Projeto Integrado do curso de Arquitetura de Software Distribuído da PUC Minas Virtual

## 1. Objetivo do projeto
Esse projeto tem por objetivo demonstar a efetividade da utilização de uma solução sem servidor (_serverless_) para realizar o processamento e carga de grandes volumes de arquivos.

## 2. Estrutura do repositório
Dentro desse repositório estão os códigos desenvolvidos para as três aplicações que compõem a solução sendo elas:

  ### 2.1. frontend:
  Aplicação para que os usuários humanos possam interagir com a solução, realizando o carregamento dos arquivos e acompanhando a situação dos processamentos.

  ### 2.2. backend:
  Conjunto de APIs utilizadas pelo frontend para carregamento de arquivos, iniciação de processamentos, obtenção de informações e interação com os processos.

  ### 2.3. rotinas_importação:
  Solução _serverless_ responsável pelo processamento dos arquivos e insersão dos dados no banco de dados em que eles serão utilizados.

## 3. Fluxo da aplicação

Utilizando o frontend os usuários podem realizar o carregamento dos arquivos a serem processados para o backend que irá, por sua vez, enviá-los par uma solução de armazenamento de blocos (_blob storage_). O carregamento dos arquivos nessa solução irá disparar um gatilho correspondente nas rotinas de importação que, por sua vez, irão realizar a leitura e os devidos tratamentos no conteúdo do arquivo.

Durante o processamento pelas rotinas de importação, atualizações sobre o andamento do mesmo serão enviadas para o backend, permitindo também que o frontend consulte as atualizações através de _endpoints_ específicos.

Também é possível aos usuários interromperem o processamento de um arquivo específico, enviado ao comandos de cancelamento ao backend através do frontend. Esses comandos são enviados para uma fila que será lida pelas rotinas de importação no momento mais apropriado dentro do laço de processamento.

## 4. Executando localmente

Os seguintes recursos são necessários para a execução local da solução:

- Docker v20
- Docker Compose v2
- Node.JS v16LTS
- NPM v8
- JDK v17
- Maven v3
- .Net Framework v6
- Azure Functions Core Tools v4

Após todas as dependências instaladas, inicie os contêineres de banco de dados, RabbitMQ e Azure Blob storage utilizando o arquivo `docker-compose.yaml` presente na raiz do repositório:

```shell script
docker compose up -d
ou
docker-compose up -d
```

Assim que as instâncias estiverem disponíveis as aplicações podem ser iniciadas, podendo ser utilizada uma instância do terminal para cada uma.

- Para as rotinas de importação:
Adicionando documentação no README
O serviço será iniciado e aguardará os gatilhos vindos do _blob storage_

- Para o backend:
```shell script
cd backend
mvn clean build quarkus:dev
```
A API será iniciada e disponibilizada na porta 8080

- Para o frontend:
```shell script
cd frontend
npm start
```
Uma aplicação React será iniciada na porta 3000