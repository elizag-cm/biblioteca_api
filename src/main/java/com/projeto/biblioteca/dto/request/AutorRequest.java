package com.projeto.biblioteca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AutorRequest(
        @NotBlank String nome,
        String biografia
) {
}