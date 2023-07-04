CREATE SCHEMA IF NOT EXISTS avalia_unb;

--------------------------------
-- SEMESTRE
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.semestre(
	ano INT NOT NULL CHECK (ano >= 0),
	numero_semestre INT NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	data_inicio DATE,
	data_fim DATE,
	PRIMARY KEY (ano, numero_semestre),
	CONSTRAINT data_valida CHECK (data_inicio < data_fim)
);

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
	eh_administrador BOOLEAN DEFAULT false,
	PRIMARY KEY (matricula)
);

--------------------------------
-- DEPARTAMENTOS
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.departamento(
	codigo INT NOT NULL CHECK (codigo >= 0),
	nome VARCHAR NOT NULL,
    ano_semestre INT NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INT NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	cor INT,
	PRIMARY KEY (codigo, ano_semestre, numero_semestre),
	FOREIGN KEY (ano_semestre, numero_semestre) REFERENCES avalia_unb.semestre(ano, numero_semestre)
);

--------------------------------
-- DISCIPLINAS
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.disciplina(
    id SERIAL NOT NULL,
	codigo VARCHAR(50) NOT NULL,
	nome VARCHAR(250) NOT NULL,
    ano_semestre INT NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INT NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	codigo_departamento INT NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id),
	FOREIGN KEY (codigo_departamento, ano_semestre, numero_semestre) REFERENCES avalia_unb.departamento(codigo, ano_semestre, numero_semestre)
);

--------------------------------
-- PROFESSORES
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.professor(
	nome VARCHAR(150) NOT NULL,
	codigo_departamento INT NOT NULL CHECK (codigo_departamento >= 0),
    ano_semestre INT NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INT NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	PRIMARY KEY (nome, codigo_departamento),
	FOREIGN KEY (codigo_departamento, ano_semestre, numero_semestre) REFERENCES avalia_unb.departamento(codigo, ano_semestre, numero_semestre)
);

--------------------------------
-- TURMAS
--------------------------------

CREATE TABLE IF NOT EXISTS avalia_unb.turma(
	id SERIAL NOT NULL,
	numero INT NOT NULL CHECK (numero >= 0),
    horario VARCHAR(50) NOT NULL,
    num_horas INT NOT NULL CHECK (num_horas >= 0),
    vagas_total INT NOT NULL CHECK (vagas_total >= 0),
    vagas_ocupadas INT NOT NULL CHECK (vagas_ocupadas >= 0 AND vagas_ocupadas <= vagas_total),
    local_aula VARCHAR(100),
    nome_professor VARCHAR(250) NOT NULL,
	id_disciplina INT NOT NULL,
	codigo_departamento INT NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id),
	FOREIGN KEY (nome_professor, codigo_departamento) REFERENCES avalia_unb.professor(nome, codigo_departamento),
	FOREIGN KEY (id_disciplina) REFERENCES avalia_unb.disciplina(id)
);