CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('ALUNO','BIBLIOTECARIO')),
    matricula VARCHAR(50) UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE autores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    biografia TEXT
);

CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao TEXT
);

CREATE TABLE livros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    categoria_id BIGINT REFERENCES categorias(id),
    autor_id BIGINT REFERENCES autores(id),
    quantidade_total INT NOT NULL DEFAULT 1,
    quantidade_disponivel INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE emprestimos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    livro_id BIGINT NOT NULL REFERENCES livros(id),
    data_emprestimo TIMESTAMP NOT NULL DEFAULT now(),
    data_prevista_devolucao TIMESTAMP NOT NULL,
    data_devolucao TIMESTAMP,
    prorrogado BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO' CHECK (status IN ('ATIVO','DEVOLVIDO','ATRASADO'))
);

CREATE TABLE justificativas (
    id BIGSERIAL PRIMARY KEY,
    emprestimo_id BIGINT NOT NULL REFERENCES emprestimos(id),
    texto TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE','APROVADA','REJEITADA')),
    data_envio TIMESTAMP NOT NULL DEFAULT now(),
    data_analise TIMESTAMP,
    bibliotecario_id BIGINT REFERENCES usuarios(id)
);

CREATE TABLE multas (
    id BIGSERIAL PRIMARY KEY,
    emprestimo_id BIGINT NOT NULL REFERENCES emprestimos(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    valor NUMERIC(10,2) NOT NULL DEFAULT 5.00,
    meses_acumulados INT NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE','PAGA','ISENTADA')),
    data_geracao TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE reservas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    livro_id BIGINT NOT NULL REFERENCES livros(id),
    data_reserva TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA' CHECK (status IN ('PENDENTE','NOTIFICADA','ATENDIDA','CANCELADA','EXPIRADA')),
    posicao_fila INT NOT NULL,
    data_notificacao TIMESTAMP,
    prazo_confirmacao TIMESTAMP
);
