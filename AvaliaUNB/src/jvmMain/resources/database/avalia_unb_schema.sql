
--------------------------------
-- SEMESTRE
--------------------------------

CREATE TABLE IF NOT EXISTS semestre(
	ano INTEGER NOT NULL CHECK (ano >= 0),
	numero_semestre INTEGER NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	data_inicio TEXT,
	data_fim TEXT,
	PRIMARY KEY (ano, numero_semestre)
);

--------------------------------
-- ESTUDANTES / USUARIOS 
--------------------------------

CREATE TABLE IF NOT EXISTS usuario(
	matricula TEXT NOT NULL CHECK (length(matricula) = 9),
	nome TEXT NOT NULL,
	curso TEXT,
	email TEXT UNIQUE NOT NULL,
	senha TEXT NOT NULL,
	foto_de_perfil BLOB,
	eh_administrador BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (matricula)
);

--------------------------------
-- DEPARTAMENTOS
--------------------------------

CREATE TABLE IF NOT EXISTS departamento(
	codigo INTEGER NOT NULL CHECK (codigo >= 0),
	nome TEXT NOT NULL,
    ano_semestre INTEGER NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INTEGER NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	cor INTEGER,
	PRIMARY KEY (codigo, ano_semestre, numero_semestre),
	FOREIGN KEY (ano_semestre, numero_semestre) REFERENCES semestre(ano, numero_semestre)
);

--------------------------------
-- DISCIPLINAS
--------------------------------

CREATE TABLE IF NOT EXISTS disciplina(
    id INTEGER NOT NULL,
	codigo TEXT NOT NULL,
	nome TEXT NOT NULL,
    ano_semestre INTEGER NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INTEGER NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	codigo_departamento INTEGER NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id),
	FOREIGN KEY (codigo_departamento, ano_semestre, numero_semestre) REFERENCES departamento(codigo, ano_semestre, numero_semestre)
);

--------------------------------
-- PROFESSORES
--------------------------------

CREATE TABLE IF NOT EXISTS professor(
	nome TEXT NOT NULL,
	codigo_departamento INTEGER NOT NULL CHECK (codigo_departamento >= 0),
    ano_semestre INTEGER NOT NULL CHECK (ano_semestre >= 0),
	numero_semestre INTEGER NOT NULL CHECK (numero_semestre = 1 OR numero_semestre = 2),
	foto_de_perfil BLOB,
	pontuacao REAL CHECK (pontuacao IS NULL OR (pontuacao >= 0 AND pontuacao <= 5)),
	PRIMARY KEY (nome, codigo_departamento),
	FOREIGN KEY (codigo_departamento, ano_semestre, numero_semestre) REFERENCES departamento(codigo, ano_semestre, numero_semestre)
);

--------------------------------
-- TURMAS
--------------------------------

CREATE TABLE IF NOT EXISTS turma(
	id INTEGER NOT NULL,
	codigo_turma TEXT,
    horario TEXT,
    num_horas INTEGER CHECK (num_horas >= 0),
    vagas_total INTEGER CHECK (vagas_total >= 0),
    vagas_ocupadas INTEGER CHECK (vagas_ocupadas >= 0 AND vagas_ocupadas <= vagas_total),
    local_aula TEXT,
    pontuacao REAL CHECK (pontuacao IS NULL OR (pontuacao >= 0 AND pontuacao <= 5)),
    nome_professor TEXT NOT NULL,
	id_disciplina INTEGER NOT NULL,
	codigo_departamento INTEGER NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id),
	FOREIGN KEY (nome_professor, codigo_departamento) REFERENCES professor(nome, codigo_departamento),
	FOREIGN KEY (id_disciplina) REFERENCES disciplina(id)
);

--------------------------------
-- AVALIACOES
--------------------------------

CREATE TABLE IF NOT EXISTS avaliacao(
	id INTEGER NOT NULL,
	comentario TEXT,
	pontuacao INTEGER NOT NULL CHECK (pontuacao >= 0 AND pontuacao <= 5),
	matricula_aluno TEXT NOT NULL CHECK (length(matricula_aluno) = 9),
	PRIMARY KEY (id),
	FOREIGN KEY (matricula_aluno) REFERENCES usuario(matricula)
);

CREATE TABLE IF NOT EXISTS avaliacao_turma(
	id_avaliacao INTEGER NOT NULL,
	id_turma INTEGER NOT NULL,
	PRIMARY KEY (id_avaliacao),
	FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id),
	FOREIGN KEY (id_turma) REFERENCES turma(id)
);

CREATE TABLE IF NOT EXISTS avaliacao_professor(
	id_avaliacao INTEGER NOT NULL,
	nome_professor TEXT NOT NULL,
	codigo_departamento INTEGER NOT NULL CHECK (codigo_departamento >= 0),
	PRIMARY KEY (id_avaliacao),
	FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id),
	FOREIGN KEY (nome_professor, codigo_departamento) REFERENCES professor(nome, codigo_departamento)
);

--------------------------------
-- DENUNCIAS
--------------------------------

CREATE TABLE IF NOT EXISTS denuncia(
	id_avaliacao INTEGER NOT NULL,
	matricula_aluno TEXT NOT NULL CHECK (length(matricula_aluno) = 9),
	descricao TEXT NOT NULL,
	PRIMARY KEY (id_avaliacao, matricula_aluno),
	FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id),
	FOREIGN KEY (matricula_aluno) REFERENCES usuario(matricula)
);

--------------------------------
-- VIEWS
--------------------------------

CREATE VIEW IF NOT EXISTS TURMAS_INFORMACOES AS
SELECT turma.*, semestre.*, disciplina.nome AS disc_nome, disciplina.codigo AS disc_cod,
    departamento.cor AS dept_cor, departamento.nome AS dept_nome,
    (SELECT COUNT(id_avaliacao)
    FROM avaliacao_turma
    WHERE id_turma = turma.id) AS num_avaliacoes
FROM turma
INNER JOIN disciplina ON turma.id_disciplina = disciplina.id
INNER JOIN departamento
ON disciplina.codigo_departamento = departamento.codigo AND
    disciplina.numero_semestre = departamento.numero_semestre AND
    disciplina.ano_semestre = departamento.ano_semestre
INNER JOIN semestre
    ON semestre.ano = disciplina.ano_semestre AND
    semestre.numero_semestre = disciplina.numero_semestre
    ;

CREATE VIEW IF NOT EXISTS PROFESSORES_INFORMACOES AS
SELECT professor.*, departamento.nome AS dept_nome,
    (SELECT COUNT(id_avaliacao)
    FROM avaliacao_professor
    WHERE nome_professor = professor.nome
    AND codigo_departamento = professor.codigo_departamento) AS num_avaliacoes
FROM professor
INNER JOIN departamento
    ON professor.codigo_departamento = departamento.codigo
    AND professor.ano_semestre = departamento.ano_semestre
    AND professor.numero_semestre = departamento.numero_semestre;

--------------------------------
-- TRIGGERS
--------------------------------

-- Trigger para remover avalicoes de turmas, avaliacoes de professores e denuncias quando uma avaliacao
-- for removida

CREATE TRIGGER IF NOT EXISTS analise_deletada
AFTER DELETE ON avaliacao
BEGIN
    DELETE FROM avaliacao_turma WHERE id_avaliacao = OLD.id;
    DELETE FROM avaliacao_professor WHERE id_avaliacao = OLD.id;
    DELETE FROM denuncia WHERE id_avaliacao = OLD.id;
END;

-- Esses triggers atualizam a pontuacao da turma e professor toda vez que uma
-- avalicao for inserida/removida/modificada

-- TURMAS -------------------------------------------------------

CREATE TRIGGER IF NOT EXISTS analise_inserida_atualizar_turma
AFTER INSERT ON avaliacao_turma
BEGIN
    UPDATE turma
    SET pontuacao = (
        SELECT AVG(pontuacao)
        FROM avaliacao
        INNER JOIN avaliacao_turma ON avaliacao.id = avaliacao_turma.id_avaliacao
        WHERE avaliacao_turma.id_turma = turma.id
    )
    WHERE turma.id = NEW.id_turma;
END;

CREATE TRIGGER IF NOT EXISTS analise_removida_atualizar_turma
AFTER DELETE ON avaliacao_turma
BEGIN
    UPDATE turma
    SET pontuacao = (
        SELECT AVG(pontuacao)
        FROM avaliacao
        INNER JOIN avaliacao_turma ON avaliacao.id = avaliacao_turma.id_avaliacao
        WHERE avaliacao_turma.id_turma = turma.id
    )
    WHERE turma.id = OLD.id_turma;
END;

-- PROFESSORES -------------------------------------------------------

CREATE TRIGGER IF NOT EXISTS analise_inserida_atualizar_professor
AFTER INSERT ON avaliacao_professor
BEGIN
    UPDATE professor
    SET pontuacao = (
        SELECT AVG(pontuacao)
        FROM avaliacao
        INNER JOIN avaliacao_professor ON avaliacao.id = avaliacao_professor.id_avaliacao
        WHERE avaliacao_professor.nome_professor = professor.nome AND
        avaliacao_professor.codigo_departamento = professor.codigo_departamento
    )
    WHERE professor.nome = NEW.nome_professor AND professor.codigo_departamento = NEW.codigo_departamento;
END;

CREATE TRIGGER IF NOT EXISTS analise_removida_atualizar_professor
AFTER DELETE ON avaliacao_professor
BEGIN
    UPDATE professor
    SET pontuacao = (
        SELECT AVG(pontuacao)
        FROM avaliacao
        INNER JOIN avaliacao_professor ON avaliacao.id = avaliacao_professor.id_avaliacao
        WHERE avaliacao_professor.nome_professor = professor.nome AND
        avaliacao_professor.codigo_departamento = professor.codigo_departamento
    )
    WHERE professor.nome = OLD.nome_professor AND professor.codigo_departamento = OLD.codigo_departamento;
END;

-- AMBOS -------------------------------------------------------

CREATE TRIGGER IF NOT EXISTS analise_modificada_atualizar_pontuacao
AFTER UPDATE ON avaliacao
BEGIN
    UPDATE turma
    SET pontuacao = (
        SELECT AVG(pontuacao)
        FROM avaliacao
        INNER JOIN avaliacao_turma ON avaliacao.id = avaliacao_turma.id_avaliacao
        WHERE avaliacao_turma.id_turma = turma.id
    )
    WHERE turma.id IN (
        SELECT id_turma
        FROM avaliacao_turma
        WHERE id_avaliacao = NEW.id);

    UPDATE professor
        SET pontuacao = (
            SELECT AVG(pontuacao)
            FROM avaliacao
            INNER JOIN avaliacao_professor ON avaliacao.id = avaliacao_professor.id_avaliacao
            WHERE avaliacao_professor.nome_professor = professor.nome AND
            avaliacao_professor.codigo_departamento = professor.codigo_departamento
        )
        WHERE (professor.nome, professor.codigo_departamento) IN (
            SELECT professor.nome, professor.codigo_departamento
            FROM avaliacao_professor
            WHERE id_avaliacao = NEW.id);
END