package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import br.ifsp.virtual_studies.dto.exercise.ExerciseResponseDTO;
import br.ifsp.virtual_studies.dto.exercise.ExercisePatchDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Operation(summary = "Buscar exercício")
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id, authentication.getUser()));
    }

    @Operation(summary = "Atualizar exercício")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(@PathVariable Long id,
            @Valid @RequestBody ExercisePatchDTO exerciseDto,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        ExerciseResponseDTO updatedExercise = exerciseService.updateExercise(id, exerciseDto, authentication.getUser());
        return ResponseEntity.ok(updatedExercise);
    }

    @Operation(summary = "Excluir exercício")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        exerciseService.deleteExercise(id, authentication.getUser());
        return ResponseEntity.noContent().build();
    }
}
