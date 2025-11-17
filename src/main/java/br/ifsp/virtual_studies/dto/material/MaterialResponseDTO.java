package br.ifsp.virtual_studies.dto.material;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Chat;
import lombok.Data;

@Data
public class MaterialResponseDTO {
    
    private Long id;
    private String title;
    private String description;
    private Long chatId;
    private String local;
    private LocalDateTime createdAt;
}
