
-- CRIAÇÃO DO SCHEMA
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8;
USE `mydb`;

-- TABELA: Usuarios
CREATE TABLE IF NOT EXISTS `Usuarios` (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (id_usuario),
  UNIQUE (email)
);

-- TABELA: Locais
CREATE TABLE IF NOT EXISTS `Locais` (
  id_local INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  endereco VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_local)
);

-- TABELA: Categorias
CREATE TABLE IF NOT EXISTS `Categorias` (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(50) NOT NULL,
  PRIMARY KEY (id_categoria)
);

-- TABELA: Eventos
CREATE TABLE IF NOT EXISTS `Eventos` (
  id_evento INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  data DATE NOT NULL,
  id_local INT,
  id_categoria INT,
  Eventoscol VARCHAR(45),
  PRIMARY KEY (id_evento),
  FOREIGN KEY (id_local) REFERENCES Locais(id_local),
  FOREIGN KEY (id_categoria) REFERENCES Categorias(id_categoria)
);

-- TABELA: Ingressos
CREATE TABLE IF NOT EXISTS `Ingressos` (
  id_ingresso INT NOT NULL AUTO_INCREMENT,
  id_evento INT,
  id_usuario INT,
  preco DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id_ingresso),
  FOREIGN KEY (id_evento) REFERENCES Eventos(id_evento),
  FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- TABELA: Usuarios_Eventos
CREATE TABLE IF NOT EXISTS `Usuarios_Eventos` (
  id_usuario INT NOT NULL,
  id_evento INT NOT NULL,
  PRIMARY KEY (id_usuario, id_evento),
  FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
  FOREIGN KEY (id_evento) REFERENCES Eventos(id_evento)
);

-- INSERÇÕES
INSERT INTO Usuarios (nome, email) VALUES
  ('Lucas', 'lucas@email.com'),
  ('Ana', 'ana@email.com'),
  ('Bruno', 'bruno@email.com');

INSERT INTO Locais (nome, endereco) VALUES
  ('Teatro Municipal', 'Rua A, 123'),
  ('Centro de Convenções', 'Av. B, 456');

INSERT INTO Categorias (nome) VALUES
  ('Música'), ('Palestra');

INSERT INTO Eventos (nome, data, id_local, id_categoria) VALUES
  ('Concerto A', '2025-05-01', 1, 1),
  ('Talk X', '2025-05-15', 2, 2);

INSERT INTO Ingressos (id_evento, id_usuario, preco) VALUES
  (1, 3, 50.00),
  (1, 3, 75.00);

INSERT INTO Usuarios_Eventos (id_usuario, id_evento) VALUES
  (3, 1);

-- VIEW
CREATE VIEW vw_relatorio_ingressos AS
SELECT u.nome AS usuario, e.nome AS evento, i.preco
FROM Ingressos i
JOIN Usuarios u ON i.id_usuario = u.id_usuario
JOIN Eventos e ON i.id_evento = e.id_evento;

-- PROCEDURE
DELIMITER //
CREATE PROCEDURE sp_criar_evento (
    IN p_nome VARCHAR(100),
    IN p_data DATE,
    IN p_id_local INT,
    IN p_id_categoria INT
)
BEGIN
    INSERT INTO Eventos (nome, data, id_local, id_categoria)
    VALUES (p_nome, p_data, p_id_local, p_id_categoria);
END //
DELIMITER ;
