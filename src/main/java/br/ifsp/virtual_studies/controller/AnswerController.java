package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.answer.AnswerResponseDTO;
import br.ifsp.virtual_studies.dto.answer.AnswerRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.service.AnswerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    private final AnswerService answerService;
    
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }
    
    @PostMapping
    public ResponseEntity<AnswerResponseDTO> createAnswer(@Valid @RequestBody AnswerRequestDTO answer) {
        AnswerResponseDTO answerResponseDTO = answerService.createAnswer(answer);
        return ResponseEntity.status(HttpStatus.CREATED).body(answerResponseDTO);
    }
    
    @GetMapping
    public ResponseEntity<PagedResponse<AnswerResponseDTO>> getAllAnswers(Pageable pageable) {
        return ResponseEntity.ok(answerService.getAllAnswers(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(@PathVariable Long id,
            @Valid @RequestBody AnswerRequestDTO answerDto) {
        AnswerResponseDTO updatedAnswer = answerService.updateAnswer(id, answerDto);
        return ResponseEntity.ok(updatedAnswer);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }
}
