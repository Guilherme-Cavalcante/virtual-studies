package br.ifsp.virtual_studies.dto.answer;

import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Student;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequestDTO {

    @NotNull
    private Exercise exercise;

    @NotNull
    private Student student;

    @NotNull
    private Double grade;
}
