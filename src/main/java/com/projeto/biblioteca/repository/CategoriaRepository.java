package com.projeto.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}