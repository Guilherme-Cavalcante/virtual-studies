package br.ifsp.virtual_studies.dto.answer;

import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Student;
import lombok.Data;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerPatchDTO {

    private Optional<Exercise> exercise;
    private Optional<Student> student;
    private Optional<Double> grade;
}
