package br.ifsp.virtual_studies.dto.chat;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatResponseDTO {

    private Long id;
    private String subject;
    private Long teacherId;
    private LocalDateTime createdAt;
}
