package com.projeto.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Multa;
import com.projeto.biblioteca.entity.StatusMulta;

public interface MultaRepository
        extends JpaRepository<Multa, Long> {

    Optional<Multa> findByEmprestimoId(Long emprestimoId);

    List<Multa> findByUsuarioIdAndStatus(
            Long usuarioId,
            StatusMulta status
    );
}
