package br.ifsp.virtual_studies.dto.chat;

import java.util.List;
import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import lombok.Data;

@Data
public class ChatResponseDTO {
    
    private Long id;
    private String subject;
    private Long teacherId;
    private List<Long> studentsIds;
    private List<Long> messagesIds;
    private LocalDateTime createdAt;
}
