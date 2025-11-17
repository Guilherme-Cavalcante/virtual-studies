package br.ifsp.virtual_studies.dto.thanks;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import lombok.Data;

@Data
public class ThanksResponseDTO {

    private Long id;
    private Message message;
    private Student student;
    private LocalDateTime createdAt;
}
