package br.ifsp.ms_gamification.application.usecases;

import org.springframework.stereotype.Service;

import br.ifsp.ms_gamification.application.repositories.ScoreRepository;
import br.ifsp.ms_gamification.domain.entities.Score;

@Service
public class AddPointsUseCase {

    private final ScoreRepository scoreRepository;

    public AddPointsUseCase(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Score execute(Long studentId, int points) {
        Score score = scoreRepository.findByStudentId(studentId);

        if (score == null) {
            score = new Score(studentId, 0);
        }

        score.addPoints(points);
        return scoreRepository.save(score);
    }
}
