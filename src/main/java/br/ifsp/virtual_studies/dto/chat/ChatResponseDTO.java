package br.ifsp.virtual_studies.dto.chat;

import java.time.LocalDateTime;
import java.util.Set;

import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import lombok.Data;

@Data
public class ChatResponseDTO {
    
    private Long id;
    private String subject;
    private Teacher teacher;
    private Set<Student> studentsList;
    private LocalDateTime createdAt;
}
