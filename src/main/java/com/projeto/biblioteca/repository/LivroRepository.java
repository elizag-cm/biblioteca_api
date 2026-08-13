package com.projeto.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long> {
}