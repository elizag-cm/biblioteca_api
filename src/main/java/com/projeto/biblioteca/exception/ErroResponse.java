package com.projeto.biblioteca.exception;

public record ErroResponse(
    String mensagem,
    int status
) {
    
}
