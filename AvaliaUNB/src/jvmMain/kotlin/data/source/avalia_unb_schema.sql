CREATE SCHEMA IF NOT EXISTS avalia_unb;

--------------------------------
-- ESTUDANTES / USUARIOS 
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.usuario(
	matricula CHAR(9) NOT NULL,
	nome VARCHAR(100) NOT NULL,
	curso VARCHAR(100),
	email VARCHAR(100) UNIQUE NOT NULL,
	senha VARCHAR(100) NOT NULL,
	foto_de_perfil BYTEA,
	PRIMARY KEY (matricula)
);