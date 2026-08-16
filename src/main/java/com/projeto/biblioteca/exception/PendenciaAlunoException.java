package com.projeto.biblioteca.exception;

public class PendenciaAlunoException extends BusinessException {

    public PendenciaAlunoException(String message) {
        super("Aluno possui multa ou empréstimos em atraso.");
    }
    
}
