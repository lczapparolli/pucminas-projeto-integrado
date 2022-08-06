CREATE TABLE `importacao_distribuida`.SITUACAO (
    situacao_id int auto_increment NOT NULL,
    descricao varchar(250) NOT NULL,
    CONSTRAINT SITUACAO_PK PRIMARY KEY (situacao_id)
);

CREATE TABLE `importacao_distribuida`.IMPORTACAO (
    importacao_id INT auto_increment NOT NULL,
    layout_id int NOT NULL,
    situacao_id int NOT NULL,
    nome_arquivo varchar(500) NOT NULL,
    inicio_importacao timestamp NOT NULL,
    fim_importacao timestamp NULL,
    CONSTRAINT IMPORTACAO_PK PRIMARY KEY (importacao_id),
    CONSTRAINT IMPORTACAO_LAYOUT_FK FOREIGN KEY (layout_id) REFERENCES `importacao_distribuida`.LAYOUT(layout_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT IMPORTACAO_SITUACAO_FK FOREIGN KEY (situacao_id) REFERENCES `importacao_distribuida`.SITUACAO(situacao_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO `importacao_distribuida`.SITUACAO (descricao) values ('PROCESSANDO'), ('PAUSADA'), ('CANCELADA'), ('SUCESSO'), ('ERRO');