package br.ifsp.virtual_studies.dto.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MessageResponseDTO {

    private Long id;
    private Long chatId;
    private Long authorId;
    private String text;
    private LocalDateTime createdAt;
}
