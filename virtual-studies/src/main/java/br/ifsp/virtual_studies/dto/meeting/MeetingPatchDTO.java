package br.ifsp.virtual_studies.dto.meeting;

import java.time.LocalDateTime;
import java.util.Optional;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class MeetingPatchDTO {
    
    Optional<String> title = Optional.empty();
    Optional<String> description = Optional.empty();
    Optional<Chat> chat = Optional.empty();
    Optional<String> link = Optional.empty();
    Optional<LocalDateTime> date = Optional.empty();
    Optional<Boolean> closed = Optional.empty();
}
