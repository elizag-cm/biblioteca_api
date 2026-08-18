package com.projeto.biblioteca.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroRequest(

        @NotBlank
        String titulo,

        @NotBlank
        String isbn,

        @NotNull
        Integer quantidadeTotal,

        @NotNull
        Long autorId,

        @NotNull
        Long categoriaId

) {
}