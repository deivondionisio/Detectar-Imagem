CREATE DATABASE clientes;

USE clientes;

CREATE TABLE clientes (
  id INT AUTO_INCREMENT NOT NULL,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  matricula VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);