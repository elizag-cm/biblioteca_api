package com.projeto.biblioteca.dto.response;

import com.projeto.biblioteca.entity.StatusJustificativa;

public record JustificativaResponse(
        Long id,
        String texto,
        StatusJustificativa status
) {
}

