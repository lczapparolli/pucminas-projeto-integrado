CREATE TABLE `importacao_distribuida`.LAYOUT (
    layout_id int auto_increment NOT NULL,
    identificacao varchar(50) NOT NULL,
    descricao varchar(250) NOT NULL,
    ativo BIT NOT NULL,
    CONSTRAINT LAYOUT_PK PRIMARY KEY (layout_id),
    UNIQUE KEY `LAYOUT_IDENTIFICACAO_UN` (`identificacao`)
);