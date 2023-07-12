--------------------------------
-- SEMESTRE
--------------------------------

INSERT INTO semestre (ano, numero_semestre, data_inicio, data_fim)
VALUES ('2022', '1', '2022-02-15', '2022-06-03');

INSERT INTO semestre (ano, numero_semestre, data_inicio, data_fim)
VALUES ('2022', '2', '2022-08-01', '2022-11-19');

INSERT INTO semestre (ano, numero_semestre, data_inicio, data_fim)
VALUES ('2023', '1', '2023-02-13', '2023-07-02');

--------------------------------
-- USUARIOS
--------------------------------

INSERT INTO usuario(matricula, nome, email, senha)
VALUES ('111111111', 'Usuário comum 01', 'aluno@exemplo.com', '1234');

INSERT INTO usuario(matricula, nome, curso, email, senha)
VALUES ('222222222', 'Usuário comum 02', 'Ciência da Computação', 'aluno@unb.com.br', '1234');

INSERT INTO usuario(matricula, nome, email, senha, eh_administrador)
VALUES ('333333333', 'Administrador 01', 'administrador@unb.com.br', '1234', '1');

--------------------------------
-- DEPARTAMENTOS
--------------------------------

INSERT INTO departamento (codigo, nome, ano_semestre, numero_semestre, cor)
VALUES ('643', 'CENTRO DE APOIO AO DESENVOLVIMENTO TECNOLÓGICO - BRASÍLIA', '2022', '1', '1688568410');

INSERT INTO departamento (codigo, nome, ano_semestre, numero_semestre, cor)
VALUES ('640', 'CENTRO DE DESENVOLVIMENTO SUSTENTÁVEL - BRASÍLIA', '2022', '2', '1684490819');

INSERT INTO departamento (codigo, nome, ano_semestre, numero_semestre, cor)
VALUES ('314', 'CENTRO DE EXCELÊNCIA EM TURISMO - BRASÍLIA', '2023', '1', '1686533299');

--------------------------------
-- DISCIPLINAS
--------------------------------

INSERT INTO disciplina (id, codigo, nome, ano_semestre, numero_semestre, codigo_departamento)
VALUES ('1', 'CDT1101', 'TECNOLOGIA SOCIAL E INOVAÇÃO', '2022', '1', '643');

INSERT INTO disciplina (id, codigo, nome, ano_semestre, numero_semestre, codigo_departamento)
VALUES ('2', 'CDS0001', 'PLANEJAMENTO E AVALIAÇÃO SOCIOAMBIENTAL', '2022', '2', '640');

INSERT INTO disciplina (id, codigo, nome, ano_semestre, numero_semestre, codigo_departamento)
VALUES ('3', 'CET0001', 'PLANEJAMENTO E GESTÃO EM TURISMO 1', '2023', '1', '314');

--------------------------------
-- PROFESSORES
--------------------------------

INSERT INTO professor (nome, codigo_departamento, ano_semestre, numero_semestre)
VALUES ('TANIA CRISTINA DA SILVA CRUZ', '643', '2022', '1');

INSERT INTO professor (nome, codigo_departamento, ano_semestre, numero_semestre)
VALUES ('LAURA ANGELICA FERREIRA DARNET', '640', '2022', '2');

INSERT INTO professor (nome, codigo_departamento, ano_semestre, numero_semestre)
VALUES ('MARUTSCHKA MARTINI MOESCH', '314', '2023', '1');

--------------------------------
-- TURMAS
--------------------------------

INSERT INTO turma (id, codigo_turma, horario, num_horas, vagas_total, vagas_ocupadas, local_aula, nome_professor,
id_disciplina, codigo_departamento)
VALUES ('1', '01', '6T2345', '30', '40', '0', 'Local à definir.', 'TANIA CRISTINA DA SILVA CRUZ', '1', '643');

INSERT INTO turma (id, codigo_turma, horario, num_horas, vagas_total, vagas_ocupadas, local_aula, nome_professor,
id_disciplina, codigo_departamento)
VALUES ('2', '01', '35N34', '60', '30', '0', 'BSA S BT 47/10', 'LAURA ANGELICA FERREIRA DARNET', '2', '640');

INSERT INTO turma (id, codigo_turma, horario, num_horas, vagas_total, vagas_ocupadas, local_aula, nome_professor,
id_disciplina, codigo_departamento)
VALUES ('3', '01', '3T2345', '60', '40', '26', 'CET - Módulo B - Sala', 'MARUTSCHKA MARTINI MOESCH', '3', '314');

--------------------------------
-- AVALIACOES
--------------------------------

-- Avaliação

INSERT INTO avaliacao (id, comentario, pontuacao, matricula_aluno)
VALUES ('1', 'Excelente turma!', '5', '111111111');

INSERT INTO avaliacao (id, pontuacao, matricula_aluno)
VALUES ('2', '4', '222222222');

INSERT INTO avaliacao (id, pontuacao, matricula_aluno)
VALUES ('3', '3', '333333333');

INSERT INTO avaliacao (id, comentario, pontuacao, matricula_aluno)
VALUES ('4', 'O professor deixou a desejar...', '2', '111111111');

INSERT INTO avaliacao (id, pontuacao, matricula_aluno)
VALUES ('5', '1', '222222222');

INSERT INTO avaliacao (id, pontuacao, matricula_aluno)
VALUES ('6', '5', '333333333');

-- Avaliação de turma

INSERT INTO avaliacao_turma (id_avaliacao, id_turma)
VALUES ('1', '1');

INSERT INTO avaliacao_turma (id_avaliacao, id_turma)
VALUES ('2', '2');

INSERT INTO avaliacao_turma (id_avaliacao, id_turma)
VALUES ('3', '3');

-- Avaliação de professor

INSERT INTO avaliacao_professor (id_avaliacao, nome_professor, codigo_departamento)
VALUES ('4', 'TANIA CRISTINA DA SILVA CRUZ', '643');

INSERT INTO avaliacao_professor (id_avaliacao, nome_professor, codigo_departamento)
VALUES ('5', 'LAURA ANGELICA FERREIRA DARNET', '640');

INSERT INTO avaliacao_professor (id_avaliacao, nome_professor, codigo_departamento)
VALUES ('6', 'MARUTSCHKA MARTINI MOESCH', '314')














