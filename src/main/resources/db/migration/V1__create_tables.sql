CREATE TABLE LOCAL (
    id_local NUMBER PRIMARY KEY,
    nome_local VARCHAR2(100) NOT NULL,
    endereco VARCHAR2(200)
);

CREATE TABLE TRANSPORTADOR (
    id_transportador NUMBER PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    cnpj VARCHAR2(20) UNIQUE NOT NULL,
    licenca_ambiental VARCHAR2(50)
);

CREATE TABLE RESIDUO (
    id_residuo NUMBER PRIMARY KEY,
    tipo VARCHAR2(50) NOT NULL,
    quantidade NUMBER(10,2) NOT NULL,
    data_geracao DATE NOT NULL,
    id_local NUMBER NOT NULL,
    CONSTRAINT fk_residuo_local FOREIGN KEY (id_local) REFERENCES LOCAL(id_local)
);

CREATE TABLE COLETA (
    id_coleta NUMBER PRIMARY KEY,
    data_coleta DATE NOT NULL,
    status VARCHAR2(20) NOT NULL,
    id_residuo NUMBER NOT NULL,
    id_transportador NUMBER NOT NULL,
    CONSTRAINT fk_coleta_residuo FOREIGN KEY (id_residuo) REFERENCES RESIDUO(id_residuo),
    CONSTRAINT fk_coleta_transportador FOREIGN KEY (id_transportador) REFERENCES TRANSPORTADOR(id_transportador)
);

CREATE TABLE DESTINO_FINAL (
    id_destino NUMBER PRIMARY KEY,
    tipo_destino VARCHAR2(50) NOT NULL,
    local_destino VARCHAR2(150) NOT NULL,
    data_recebimento DATE NOT NULL,
    id_coleta NUMBER NOT NULL,
    CONSTRAINT fk_destino_coleta FOREIGN KEY (id_coleta) REFERENCES COLETA(id_coleta)
);

CREATE TABLE LOG_ALERTA (
    id_alerta NUMBER PRIMARY KEY,
    mensagem VARCHAR2(200) NOT NULL,
    data_alerta DATE NOT NULL
);

CREATE TABLE USUARIO (
    id_usuario NUMBER PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    nome_completo VARCHAR2(150),
    role VARCHAR2(20) NOT NULL,
    ativo NUMBER(1) DEFAULT 1 NOT NULL
);