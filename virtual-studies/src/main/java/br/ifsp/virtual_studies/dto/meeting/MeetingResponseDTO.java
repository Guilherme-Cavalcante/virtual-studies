package br.ifsp.virtual_studies.dto.meeting;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class MeetingResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private Long chatId;
    private String link;
    private LocalDateTime date;
    private boolean closed;
    private LocalDateTime createdAt;
}
