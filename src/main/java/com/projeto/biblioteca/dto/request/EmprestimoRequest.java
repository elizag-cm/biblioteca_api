package com.projeto.biblioteca.dto.request;

public record EmprestimoRequest(
        Long usuarioId,
        Long livroId
) {
}