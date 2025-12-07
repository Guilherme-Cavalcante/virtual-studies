package br.ifsp.virtual_studies.dto.student;

import java.util.Optional;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPatchDTO {

    Optional<String> name = Optional.empty();

    @Email
    Optional<String> email = Optional.empty();
    Optional<String> password = Optional.empty();
    Optional<Double> score = Optional.empty();
}
