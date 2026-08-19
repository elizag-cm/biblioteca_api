package com.projeto.biblioteca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.projeto.biblioteca.dto.request.EmprestimoRequest;
import com.projeto.biblioteca.dto.response.EmprestimoResponse;
import com.projeto.biblioteca.service.EmprestimoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService service;

    @PostMapping
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<EmprestimoResponse> criar(
            @Valid @RequestBody EmprestimoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        service.criar(
                                request.usuarioId(),
                                request.livroId()
                        )
                );
    }

    @PutMapping("/{id}/devolver")
    @PreAuthorize("hasRole('BIBLIOTECARIO')")
    public ResponseEntity<EmprestimoResponse> devolver(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.devolver(id)
        );
    }

    @PutMapping("/{id}/prorrogar")
    @PreAuthorize("hasRole('ALUNO')")
    public ResponseEntity<EmprestimoResponse> prorrogar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.prorrogar(id)
        );
    }
}