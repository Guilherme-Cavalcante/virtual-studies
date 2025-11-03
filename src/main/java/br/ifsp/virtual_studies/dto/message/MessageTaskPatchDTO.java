package br.ifsp.virtual_studies.dto.message;

import java.util.Optional;

import br.ifsp.virtual_studies.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageTaskPatchDTO {

    Optional<String> text = Optional.empty();
    Optional<User> user = Optional.empty();
    Optional<String> password = Optional.empty();
}
