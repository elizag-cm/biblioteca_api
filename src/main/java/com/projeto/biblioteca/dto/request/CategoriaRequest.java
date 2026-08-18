package com.projeto.biblioteca.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(
        @NotBlank String nome
) {
}