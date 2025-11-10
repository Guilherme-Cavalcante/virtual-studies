package br.ifsp.virtual_studies.dto.thanks;

import br.ifsp.virtual_studies.model.Student;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThanksRequestDTO {

    @NotNull(message = "Campo 'message' não pode ser nulo")
    private String message;

    @NotNull(message = "Campo 'student' não pode ser nulo")
    private Student student;
}
