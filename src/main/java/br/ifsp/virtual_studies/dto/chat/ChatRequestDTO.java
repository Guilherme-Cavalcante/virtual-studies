package br.ifsp.virtual_studies.dto.chat;

import java.util.Set;

import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRequestDTO {
    
    @NotNull(message = "Campo 'subject' não pode ser nulo")
    private String subject;

    @NotNull(message = "Campo 'text' não pode ser nulo")
    private long teacherId;
}
