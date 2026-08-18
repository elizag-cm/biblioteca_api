package com.projeto.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.biblioteca.dto.request.AutorRequest;
import com.projeto.biblioteca.dto.response.AutorResponse;
import com.projeto.biblioteca.entity.Autor;
import com.projeto.biblioteca.repository.AutorRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;

    public AutorResponse criar(AutorRequest request) {

        Autor autor = new Autor();

        autor.setNome(request.nome());
        autor.setBiografia(request.biografia());

        return toResponse(repository.save(autor));
    }

    public List<AutorResponse> listarTodos() {

        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AutorResponse buscarPorId(Long id) {

        return toResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Autor não encontrado"))
        );
    }

    public AutorResponse atualizar(Long id, AutorRequest request) {

        Autor autor = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Autor não encontrado"));

        autor.setNome(request.nome());
        autor.setBiografia(request.biografia());

        return toResponse(repository.save(autor));
    }

    public void deletar(Long id) {

        Autor autor = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Autor não encontrado"));

        repository.delete(autor);
    }

    private AutorResponse toResponse(Autor autor) {

        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getBiografia()
        );
    }
}