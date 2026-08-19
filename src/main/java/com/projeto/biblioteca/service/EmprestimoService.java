package com.projeto.biblioteca.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.projeto.biblioteca.dto.response.EmprestimoResponse;
import com.projeto.biblioteca.entity.Emprestimo;
import com.projeto.biblioteca.entity.Livro;
import com.projeto.biblioteca.entity.StatusEmprestimo;
import com.projeto.biblioteca.entity.StatusMulta;
import com.projeto.biblioteca.entity.Usuario;
import com.projeto.biblioteca.exception.EmprestimoJaProrrogadoException;
import com.projeto.biblioteca.exception.ForaDoPrazoProrrogacaoException;
import com.projeto.biblioteca.exception.LimiteEmprestimoAtingidoException;
import com.projeto.biblioteca.exception.LivroIndisponivelException;
import com.projeto.biblioteca.exception.PendenciaAlunoException;
import com.projeto.biblioteca.repository.EmprestimoRepository;
import com.projeto.biblioteca.repository.LivroRepository;
import com.projeto.biblioteca.repository.MultaRepository;
import com.projeto.biblioteca.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final MultaRepository multaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaService reservaService;

    private static final int LIMITE_EMPRESTIMOS = 2;
    private static final int PRAZO_DIAS = 7;
    private static final int PRORROGACAO_DIAS = 3;

    @Transactional
    public EmprestimoResponse criar(Long usuarioId, Long livroId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuário não encontrado"));

        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Livro não encontrado"));

        if (livro.getQuantidadeDisponivel() <= 0) {
            throw new LivroIndisponivelException(
                    "Livro não possui exemplares disponíveis"
            );
        }

        long ativos = emprestimoRepository
                .countByUsuarioIdAndStatus(
                        usuarioId,
                        StatusEmprestimo.ATIVO
                );

        if (ativos >= LIMITE_EMPRESTIMOS) {
            throw new LimiteEmprestimoAtingidoException(
                    "O usuário já possui o limite de 2 empréstimos ativos"
            );
        }

        boolean temPendencia =
                !multaRepository
                        .findByUsuarioIdAndStatus(
                                usuarioId,
                                StatusMulta.PENDENTE
                        )
                        .isEmpty()
                ||
                !emprestimoRepository
                        .findByUsuarioIdAndStatus(
                                usuarioId,
                                StatusEmprestimo.ATRASADO
                        )
                        .isEmpty();

        if (temPendencia) {
            throw new PendenciaAlunoException(
                    "Usuário possui pendências e não pode realizar um novo empréstimo"
            );
        }

        livro.setQuantidadeDisponivel(
                livro.getQuantidadeDisponivel() - 1
        );

        livroRepository.save(livro);

        LocalDateTime agora = LocalDateTime.now();

        Emprestimo emprestimo = Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(agora)
                .dataPrevistaDevolucao(
                        agora.plusDays(PRAZO_DIAS)
                )
                .prorrogado(false)
                .status(StatusEmprestimo.ATIVO)
                .build();

        return toResponse(
                emprestimoRepository.save(emprestimo)
        );
    }

    @Transactional
    public EmprestimoResponse devolver(Long emprestimoId) {

        Emprestimo emprestimo = buscarOuFalhar(emprestimoId);

        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        Livro livro = emprestimo.getLivro();

        livro.setQuantidadeDisponivel(
                livro.getQuantidadeDisponivel() + 1
        );

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        reservaService.notificarProximo(livro.getId());

        return toResponse(emprestimo);
    }

    @Transactional
    public EmprestimoResponse prorrogar(Long emprestimoId) {

        Emprestimo emprestimo = buscarOuFalhar(emprestimoId);

        if (emprestimo.isProrrogado()) {
            throw new EmprestimoJaProrrogadoException(
                    "Este empréstimo já foi prorrogado."
            );
        }

        long diasRestantes =
                ChronoUnit.DAYS.between(
                        LocalDateTime.now(),
                        emprestimo.getDataPrevistaDevolucao()
                );

        if (diasRestantes > 2) {
            throw new ForaDoPrazoProrrogacaoException(
                    "Prorrogação só pode ser solicitada nos últimos 2 dias do prazo."
            );
        }

        emprestimo.setDataPrevistaDevolucao(
                emprestimo
                        .getDataPrevistaDevolucao()
                        .plusDays(PRORROGACAO_DIAS)
        );

        emprestimo.setProrrogado(true);

        return toResponse(
                emprestimoRepository.save(emprestimo)
        );
    }

    private Emprestimo buscarOuFalhar(Long id) {

        return emprestimoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Empréstimo não encontrado"
                        ));
    }

    private EmprestimoResponse toResponse(Emprestimo emprestimo) {

        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataPrevistaDevolucao(),
                emprestimo.getStatus()
        );
    }
}