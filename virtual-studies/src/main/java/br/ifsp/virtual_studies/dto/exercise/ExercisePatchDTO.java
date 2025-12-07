package br.ifsp.virtual_studies.dto.exercise;

import java.util.Optional;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class ExercisePatchDTO {
    
    Optional<String> title = Optional.empty();
    Optional<String> description = Optional.empty();
    Optional<String> link = Optional.empty();
}
