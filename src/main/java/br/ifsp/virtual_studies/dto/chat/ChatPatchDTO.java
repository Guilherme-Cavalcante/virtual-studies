package br.ifsp.virtual_studies.dto.chat;

import java.util.Optional;
import java.util.Set;

import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import lombok.Data;

@Data
public class ChatPatchDTO {
    
    Optional<String> subject = Optional.empty();
}
