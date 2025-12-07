package br.ifsp.virtual_studies.dto.thanks;

import java.util.Optional;

import br.ifsp.virtual_studies.model.Student;
import lombok.Data;

@Data
public class ThanksPatchDTO {

    private Optional<String> message;
    private Optional<Student> student;
}
