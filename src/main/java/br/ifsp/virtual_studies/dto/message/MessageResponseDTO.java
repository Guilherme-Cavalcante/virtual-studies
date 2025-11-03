package br.ifsp.virtual_studies.dto.message;

import br.ifsp.virtual_studies.model.User;
import lombok.Data;

@Data
public class MessageResponseDTO {

    private Long id;
    private String text;
    private User user;
    private String password;
}
