package com.projeto.biblioteca.exception;

public class LivroIndisponivelException extends BusinessException {

    public LivroIndisponivelException(String message) {
        super("Não há exemplares disponíneis para este livro.");
    }
    
}
