package com.projeto.biblioteca.security;

public record LoginRequest(
        String email,
        String senha
) {
}