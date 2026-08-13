package com.projeto.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Justificativa;

public interface JustificativaRepository extends JpaRepository<Justificativa, Long> {
}