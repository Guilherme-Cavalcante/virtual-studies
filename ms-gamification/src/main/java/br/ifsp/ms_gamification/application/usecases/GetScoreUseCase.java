package br.ifsp.ms_gamification.application.usecases;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.ifsp.ms_gamification.application.dto.score.ScoreResponseDTO;
import br.ifsp.ms_gamification.application.repositories.ScoreRepository;
import br.ifsp.ms_gamification.domain.entities.Score;
import br.ifsp.ms_gamification.exceptions.ResourceNotFoundException;

@Service
public class GetScoreUseCase {

    private final ScoreRepository scoreRepository;
    private final ModelMapper modelMapper;

    public GetScoreUseCase(ScoreRepository scoreRepository, ModelMapper modelMapper) {
        this.scoreRepository = scoreRepository;
        this.modelMapper = modelMapper;
    }
    
    public ScoreResponseDTO execute(Long studentId) {
        Score score = scoreRepository.findByStudentId(studentId);

        if (score == null) {
            throw new ResourceNotFoundException("Student not found.");
        }

        return modelMapper.map(score, ScoreResponseDTO.class);
    }
}
