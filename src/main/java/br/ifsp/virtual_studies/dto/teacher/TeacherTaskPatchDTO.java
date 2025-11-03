package br.ifsp.virtual_studies.dto.teacher;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTaskPatchDTO {

    Optional<String> name = Optional.empty();
    Optional<String> email = Optional.empty();
    Optional<String> password = Optional.empty();
}
