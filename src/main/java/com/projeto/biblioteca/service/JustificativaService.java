package com.projeto.biblioteca.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.projeto.biblioteca.dto.response.JustificativaResponse;
import com.projeto.biblioteca.entity.Emprestimo;
import com.projeto.biblioteca.entity.Justificativa;
import com.projeto.biblioteca.entity.StatusEmprestimo;
import com.projeto.biblioteca.entity.StatusJustificativa;
import com.projeto.biblioteca.entity.StatusMulta;
import com.projeto.biblioteca.entity.Usuario;
import com.projeto.biblioteca.exception.BusinessException;
import com.projeto.biblioteca.repository.EmprestimoRepository;
import com.projeto.biblioteca.repository.JustificativaRepository;
import com.projeto.biblioteca.repository.MultaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JustificativaService {

    private final JustificativaRepository justificativaRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final MultaRepository multaRepository;

    @Transactional
    public JustificativaResponse enviar(
            Long emprestimoId,
            String texto) {

        Emprestimo emprestimo =
                emprestimoRepository.findById(emprestimoId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Empréstimo não encontrado"
                                ));

        if (emprestimo.getStatus()
                != StatusEmprestimo.ATRASADO) {

            throw new BusinessException(
                    "Só é possível justificar empréstimos atrasados."
            ) {};
        }

        Justificativa justificativa =
                Justificativa.builder()
                        .emprestimo(emprestimo)
                        .texto(texto)
                        .status(StatusJustificativa.PENDENTE)
                        .dataEnvio(LocalDateTime.now())
                        .build();

        return toResponse(
                justificativaRepository.save(justificativa)
        );
    }

    @Transactional
    public JustificativaResponse aprovar(
            Long justificativaId,
            Long bibliotecarioId) {

        Justificativa justificativa =
                buscarOuFalhar(justificativaId);

        justificativa.setStatus(
                StatusJustificativa.APROVADA
        );

        justificativa.setDataAnalise(
                LocalDateTime.now()
        );

        Usuario bibliotecario = new Usuario();
        bibliotecario.setId(bibliotecarioId);

        justificativa.setBibliotecario(bibliotecario);

        multaRepository
                .findByEmprestimoId(
                        justificativa
                                .getEmprestimo()
                                .getId()
                )
                .ifPresent(multa -> {

                    multa.setStatus(
                            StatusMulta.ISENTADA
                    );

                    multaRepository.save(multa);
                });

        return toResponse(
                justificativaRepository.save(justificativa)
        );
    }

    @Transactional
    public JustificativaResponse rejeitar(
            Long justificativaId,
            Long bibliotecarioId) {

        Justificativa justificativa =
                buscarOuFalhar(justificativaId);

        justificativa.setStatus(
                StatusJustificativa.REJEITADA
        );

        justificativa.setDataAnalise(
                LocalDateTime.now()
        );

        Usuario bibliotecario = new Usuario();
        bibliotecario.setId(bibliotecarioId);

        justificativa.setBibliotecario(bibliotecario);

        return toResponse(
                justificativaRepository.save(justificativa)
        );
    }

    private Justificativa buscarOuFalhar(Long id) {

        return justificativaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Justificativa não encontrada"
                        ));
    }

    private JustificativaResponse toResponse(
            Justificativa j) {

        return new JustificativaResponse(
                j.getId(),
                j.getTexto(),
                j.getStatus()
        );
    }
}

