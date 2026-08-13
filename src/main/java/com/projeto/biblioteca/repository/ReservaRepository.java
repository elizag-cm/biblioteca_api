package com.projeto.biblioteca.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Reserva;
import com.projeto.biblioteca.entity.StatusReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findFirstByLivroIdAndStatusOrderByPosicaoFilaAsc(
            Long livroId,
            StatusReserva status
    );

    List<Reserva> findByStatusAndPrazoConfirmacaoBefore(
            StatusReserva status,
            LocalDateTime data
    );
}