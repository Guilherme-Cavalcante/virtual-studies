package br.ifsp.virtual_studies.dto.score;

public record ScoreRequestDTO (
    
    Long studentId,
    int points
) {}
