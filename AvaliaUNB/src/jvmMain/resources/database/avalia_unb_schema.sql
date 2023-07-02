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

--------------------------------
-- DEPARTAMENTOS
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.departamento(
	codigo INT NOT NULL CHECK (codigo >= 0),
	nome VARCHAR NOT NULL,
	semestre VARCHAR(6) NOT NULL,
	cor INT,
	PRIMARY KEY (codigo, semestre)
);

--------------------------------
-- DISCIPLINAS
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.disciplina(
	id SERIAL NOT NULL,
	codigo VARCHAR(50) NOT NULL,
	nome VARCHAR NOT NULL,
	semestre VARCHAR(6) NOT NULL,
	codigo_departamento INT NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id),
	FOREIGN KEY (codigo_departamento, semestre) REFERENCES avalia_unb.departamento(codigo, semestre)
);