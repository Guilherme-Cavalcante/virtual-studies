package br.ifsp.virtual_studies.dto.message;

import java.time.LocalDateTime;

import br.ifsp.virtual_studies.dto.usuario.UsuarioResponseDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Usuario;
import lombok.Data;

@Data
public class MessageResponseDTO {

    private Long id;
    private Long chatId;
    private Long authorId;
    private String text;
    private LocalDateTime createdAt;
}
