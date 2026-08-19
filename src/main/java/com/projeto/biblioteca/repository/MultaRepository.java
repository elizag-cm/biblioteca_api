package com.projeto.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Multa;
import com.projeto.biblioteca.entity.StatusMulta;

public interface MultaRepository extends JpaRepository<Multa, Long> {

    List<Multa> findByUsuarioIdAndStatus(
            Long usuarioId,
            StatusMulta status
    );
}