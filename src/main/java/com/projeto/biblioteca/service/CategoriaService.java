package com.projeto.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.biblioteca.dto.request.CategoriaRequest;
import com.projeto.biblioteca.dto.response.CategoriaResponse;
import com.projeto.biblioteca.entity.Categoria;
import com.projeto.biblioteca.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaResponse criar(CategoriaRequest request) {

        Categoria categoria = new Categoria();

        categoria.setNome(request.nome());

        return toResponse(repository.save(categoria));
    }

    public List<CategoriaResponse> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoriaResponse buscarPorId(Long id) {

        return toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Categoria não encontrada"))
        );
    }

    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {

        Categoria categoria = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada"));

        categoria.setNome(request.nome());

        return toResponse(repository.save(categoria));
    }

    public void deletar(Long id) {

        Categoria categoria = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Categoria não encontrada"));

        repository.delete(categoria);
    }

    private CategoriaResponse toResponse(Categoria categoria) {

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }
}