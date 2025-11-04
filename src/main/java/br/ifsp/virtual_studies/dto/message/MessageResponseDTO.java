package br.ifsp.virtual_studies.dto.message;

import br.ifsp.virtual_studies.model.Usuario;
import lombok.Data;

@Data
public class MessageResponseDTO {

    private Long id;
    private String text;
    private Usuario Usuario;
    private String password;
}
