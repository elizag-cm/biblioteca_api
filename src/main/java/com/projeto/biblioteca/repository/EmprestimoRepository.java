package com.projeto.biblioteca.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.biblioteca.entity.Emprestimo;
import com.projeto.biblioteca.entity.StatusEmprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuarioId(
            Long usuarioId,
            StatusEmprestimo status
    );
    long countByUsuarioIdAndStatus(
            Long usuarioId,
            StatusEmprestimo status
    );

    List<Emprestimo> findByStatusAndDataPrevistaDevolucaoBefore( 
        
            StatusEmprestimo status, 
            LocalDateTime data
    );
}
