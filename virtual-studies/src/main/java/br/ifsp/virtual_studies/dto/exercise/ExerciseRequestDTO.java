package br.ifsp.virtual_studies.dto.exercise;
                
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExerciseRequestDTO {
    
    @NotNull(message = "Campo 'title' não pode ser nulo")
    private String title;
    
    @NotNull(message = "Campo 'description' não pode ser nulo")
    private String description;
    
    @NotNull(message = "Campo 'link' não pode ser nulo")
    private String link;
}
