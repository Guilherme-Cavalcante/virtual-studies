package br.ifsp.virtual_studies.dto.message;

import br.ifsp.virtual_studies.model.Usuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequestDTO {

    @NotNull(message = "Campo 'text' não pode ser nulo")
    private String text;

    @NotNull(message = "Campo 'email' não pode ser nulo")
    private Usuario Usuario;
}
