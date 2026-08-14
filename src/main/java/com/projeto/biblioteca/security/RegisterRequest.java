package com.projeto.biblioteca.security;

import com.projeto.biblioteca.entity.TipoUsuario;

public record RegisterRequest(
        String nome,
        String email,
        String senha,
        TipoUsuario tipo,
        String matricula
) {
}