package br.ifsp.virtual_studies.dto.message;

import java.util.Optional;

import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePatchDTO {

    Optional<Chat> password = Optional.empty();
    Optional<Usuario> author = Optional.empty();
    Optional<String> text = Optional.empty();
}
