package br.ifsp.virtual_studies.dto.answer;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Student;
import lombok.Data;

@Data
public class AnswerResponseDTO {

    private Long id;
    private Exercise exercise;
    private Student student;
    private Double grade;
    private LocalDateTime createdAt;
}
