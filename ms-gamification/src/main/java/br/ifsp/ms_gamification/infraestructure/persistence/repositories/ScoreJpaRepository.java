package br.ifsp.ms_gamification.infraestructure.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.ms_gamification.infraestructure.persistence.entities.ScoreJpaEntity;

public interface ScoreJpaRepository extends JpaRepository<ScoreJpaEntity, Long> {
    Optional<ScoreJpaEntity> findByStudentId(Long studentId);
}
