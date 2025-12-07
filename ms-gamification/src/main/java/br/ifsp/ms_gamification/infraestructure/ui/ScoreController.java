package br.ifsp.ms_gamification.infraestructure.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.ms_gamification.application.dto.score.ScoreRequestDTO;
import br.ifsp.ms_gamification.application.dto.score.ScoreResponseDTO;
import br.ifsp.ms_gamification.application.usecases.AddPointsUseCase;
import br.ifsp.ms_gamification.application.usecases.GetScoreUseCase;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
    
    private final AddPointsUseCase addPointsUseCase;
    private final GetScoreUseCase getScoreUseCase;

    public ScoreController(AddPointsUseCase addPointsUseCase, GetScoreUseCase getScoreUseCase) {
        this.addPointsUseCase = addPointsUseCase;
        this.getScoreUseCase = getScoreUseCase;
    }   
    
    @PostMapping
    public ResponseEntity<Void> addScore(@RequestBody ScoreRequestDTO score) {
        addPointsUseCase.execute(score.studentId(), score.points());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ScoreResponseDTO> getScore(@PathVariable Long studentId) {
        ScoreResponseDTO score = getScoreUseCase.execute(studentId);
        return ResponseEntity.ok(score);
    }
}
