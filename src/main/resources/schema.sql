
CREATE TABLE funcionario (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);


CREATE TABLE funcionario_roles (
    funcionario_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id) ON DELETE CASCADE,
    PRIMARY KEY (funcionario_id, role)
);


CREATE TABLE morador (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    apartamento VARCHAR(10)
);


CREATE TABLE morador_roles (
    morador_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (morador_id) REFERENCES morador(id) ON DELETE CASCADE,
    PRIMARY KEY (morador_id, role)
);
