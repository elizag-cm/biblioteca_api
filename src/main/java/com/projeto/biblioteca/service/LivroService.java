package com.projeto.biblioteca.service;

import com.projeto.biblioteca.dto.request.LivroRequest;
import com.projeto.biblioteca.dto.response.LivroResponse;
import com.projeto.biblioteca.entity.Autor;
import com.projeto.biblioteca.entity.Categoria;
import com.projeto.biblioteca.entity.Livro;
import com.projeto.biblioteca.repository.AutorRepository;
import com.projeto.biblioteca.repository.CategoriaRepository;
import com.projeto.biblioteca.repository.LivroRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroResponse criar(LivroRequest request) {

        Autor autor = autorRepository.findById(request.autorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada"));

        Livro livro = new Livro();

        livro.setTitulo(request.titulo());
        livro.setIsbn(request.isbn());
        livro.setQuantidadeTotal(request.quantidadeTotal());
        livro.setQuantidadeDisponivel(request.quantidadeTotal());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        return toResponse(repository.save(livro));
    }

    public List<LivroResponse> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LivroResponse buscarPorId(Long id) {

        return toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Livro não encontrado"))
        );
    }

    public LivroResponse atualizar(Long id, LivroRequest request) {

        Livro livro = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Livro não encontrado"));

        Autor autor = autorRepository.findById(request.autorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Autor não encontrado"));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada"));

        livro.setTitulo(request.titulo());
        livro.setIsbn(request.isbn());
        livro.setQuantidadeTotal(request.quantidadeTotal());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        return toResponse(repository.save(livro));
    }

    public void deletar(Long id) {

        Livro livro = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Livro não encontrado"));

        repository.delete(livro);
    }

    private LivroResponse toResponse(Livro livro) {

        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getQuantidadeTotal(),
                livro.getQuantidadeDisponivel(),
                livro.getAutor().getId(),
                livro.getCategoria().getId()
        );
    }
}