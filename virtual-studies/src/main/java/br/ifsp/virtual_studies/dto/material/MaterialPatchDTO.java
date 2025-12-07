package br.ifsp.virtual_studies.dto.material;

import java.util.Optional;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class MaterialPatchDTO {
    
    Optional<String> title = Optional.empty();
    Optional<String> description = Optional.empty();
    Optional<Chat> chat = Optional.empty();
    Optional<String> local = Optional.empty();
}
