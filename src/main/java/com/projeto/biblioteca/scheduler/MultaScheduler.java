package com.projeto.biblioteca.scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projeto.biblioteca.entity.Emprestimo;
import com.projeto.biblioteca.entity.Multa;
import com.projeto.biblioteca.entity.StatusEmprestimo;
import com.projeto.biblioteca.entity.StatusJustificativa;
import com.projeto.biblioteca.entity.StatusMulta;
import com.projeto.biblioteca.repository.EmprestimoRepository;
import com.projeto.biblioteca.repository.JustificativaRepository;
import com.projeto.biblioteca.repository.MultaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MultaScheduler {

    private final EmprestimoRepository emprestimoRepository;
    private final MultaRepository multaRepository;
    private final JustificativaRepository justificativaRepository;

    private static final BigDecimal VALOR_BASE =
            new BigDecimal("5.00");

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void gerarOuAtualizarMultas() {

        List<Emprestimo> atrasados =
                emprestimoRepository
                        .findByStatus(
                                StatusEmprestimo.ATRASADO
                        );

        for (Emprestimo emprestimo : atrasados) {

            boolean temJustificativaAprovada =
                    justificativaRepository
                            .findByEmprestimoId(
                                    emprestimo.getId()
                            )
                            .filter(j ->
                                    j.getStatus()
                                    == StatusJustificativa.APROVADA
                            )
                            .isPresent();

            if (temJustificativaAprovada) {
                continue;
            }

            long mesesAtraso =
                    ChronoUnit.MONTHS.between(
                            emprestimo
                                    .getDataPrevistaDevolucao(),
                            LocalDateTime.now()
                    ) + 1;

            Optional<Multa> existente =
                    multaRepository
                            .findByEmprestimoId(
                                    emprestimo.getId()
                            );

            if (existente.isPresent()) {

                Multa multa = existente.get();

                if (mesesAtraso >
                        multa.getMesesAcumulados()) {

                    multa.setMesesAcumulados(
                            (int) mesesAtraso
                    );

                    multa.setValor(
                            VALOR_BASE.multiply(
                                    BigDecimal.valueOf(
                                            mesesAtraso
                                    )
                            )
                    );

                    multaRepository.save(multa);
                }

            } else {

                Multa multa =
                        Multa.builder()
                                .emprestimo(emprestimo)
                                .usuario(
                                        emprestimo.getUsuario()
                                )
                                .valor(VALOR_BASE)
                                .mesesAcumulados(1)
                                .status(
                                        StatusMulta.PENDENTE
                                )
                                .dataGeracao(
                                        LocalDateTime.now()
                                )
                                .build();

                multaRepository.save(multa);
            }
        }
    }
}