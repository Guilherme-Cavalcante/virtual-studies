package br.ifsp.virtual_studies.dto.thanks;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThanksRequestDTO {

    @NotNull(message = "Campo 'message' não pode ser nulo")
    private String message;

    @NotNull(message = "Campo 'student' não pode ser nulo")
    private Long studentId;
}
