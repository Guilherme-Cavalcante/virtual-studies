package br.ifsp.ms_gamification.infraestructure.jpa_adapters;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.ifsp.ms_gamification.application.repositories.ScoreRepository;
import br.ifsp.ms_gamification.domain.entities.Score;
import br.ifsp.ms_gamification.infraestructure.persistence.entities.ScoreJpaEntity;
import br.ifsp.ms_gamification.infraestructure.persistence.repositories.ScoreJpaRepository;

@Component
public class JpaScoreRepositoryAdapter implements ScoreRepository {

    private final ScoreJpaRepository repo;
    private final ModelMapper modelMapper;

    public JpaScoreRepositoryAdapter(ScoreJpaRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    @Override
    public Score findByStudentId(Long studentId) {
        Optional<ScoreJpaEntity> optionalScore = repo.findByStudentId(studentId);
        if (optionalScore.isEmpty()) {
            return null;
        }
        return modelMapper.map(repo.findByStudentId(studentId).get(), Score.class);
    }

    @Override
    public Score save(Score score) {
        ScoreJpaEntity scoreEntity = modelMapper.map(score, ScoreJpaEntity.class);
        return modelMapper.map(repo.save(scoreEntity), Score.class);
    }
    
}
