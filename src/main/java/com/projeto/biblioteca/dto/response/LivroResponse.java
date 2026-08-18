package com.projeto.biblioteca.dto.response;

public record LivroResponse(

        Long id,
        String titulo,
        String isbn,
        Integer quantidadeTotal,
        Integer quantidadeDisponivel,
        Long autorId,
        Long categoriaId

) {
}