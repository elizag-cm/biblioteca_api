package com.projeto.biblioteca.exception;

public class LimiteEmprestimoAtingidoException extends BusinessException {

    public LimiteEmprestimoAtingidoException(String message) {
        super("Aluno atingiu o limite de empréstimos permitidos.");
    }
    
}
