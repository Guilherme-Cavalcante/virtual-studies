package br.ifsp.virtual_studies.dto.meeting;
                
import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Chat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeetingRequestDTO {
    
    @NotNull(message = "Campo 'title' não pode ser nulo")
    private String title;
    
    @NotNull(message = "Campo 'description' não pode ser nulo")
    private String description;
    
    @NotNull(message = "Campo 'link' não pode ser nulo")
    private String link;
    
    @NotNull(message = "Campo 'date' não pode ser nulo")
    private LocalDateTime date;

    private boolean closed = false;
}
