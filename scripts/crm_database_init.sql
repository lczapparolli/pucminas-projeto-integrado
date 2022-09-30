-- crm.cliente definition

CREATE TABLE `cliente` (
  `documento` bigint NOT NULL,
  `nome` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  `data_hora_criacao` timestamp NOT NULL,
  `data_hora_atualizacao` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- crm.endereco definition

CREATE TABLE `endereco` (
  `endereco_id` bigint NOT NULL AUTO_INCREMENT,
  `documento_cliente` bigint NOT NULL,
  `logradouro` varchar(500) NOT NULL,
  `numero` varchar(100) DEFAULT NULL,
  `complemento` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `municipio` varchar(100) NOT NULL,
  `uf` varchar(2) NOT NULL,
  `cep` int DEFAULT NULL,
  `data_hora_criacao` timestamp NOT NULL,
  `data_hora_atualizacao` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`endereco_id`),
  KEY `endereco_cliente_FK` (`documento_cliente`),
  CONSTRAINT `endereco_cliente_FK` FOREIGN KEY (`documento_cliente`) REFERENCES `cliente` (`documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- crm.telefone definition

CREATE TABLE `telefone` (
  `telefone_id` bigint NOT NULL AUTO_INCREMENT,
  `documento_cliente` bigint NOT NULL,
  `ddd` int NOT NULL,
  `telefone` int NOT NULL,
  `ramal` int DEFAULT NULL,
  `data_hora_criacao` timestamp NOT NULL,
  `data_hora_atualizacao` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`telefone_id`),
  KEY `telefone_cliente_FK` (`documento_cliente`),
  CONSTRAINT `telefone_cliente_FK` FOREIGN KEY (`documento_cliente`) REFERENCES `cliente` (`documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;