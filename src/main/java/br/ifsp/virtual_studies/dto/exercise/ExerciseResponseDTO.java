package br.ifsp.virtual_studies.dto.exercise;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class ExerciseResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private Chat chat;
    private String link;
    private LocalDateTime createdAt;
}
