package com.projeto.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}