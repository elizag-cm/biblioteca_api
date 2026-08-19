package com.projeto.biblioteca.dto.response;

import java.time.LocalDateTime;

import com.projeto.biblioteca.entity.StatusEmprestimo;

public record EmprestimoResponse(
        Long id,
        String livro,
        LocalDateTime dataEmprestimo,
        LocalDateTime dataPrevistaDevolucao,
        StatusEmprestimo status
) {
}