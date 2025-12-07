package br.ifsp.ms_gamification.application.repositories;

import br.ifsp.ms_gamification.domain.entities.Score;

public interface ScoreRepository {
    public Score findByStudentId(Long studentId);
    public Score save(Score score);
}
