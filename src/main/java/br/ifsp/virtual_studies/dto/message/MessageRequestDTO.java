package br.ifsp.virtual_studies.dto.message;

import br.ifsp.virtual_studies.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequestDTO {

    @NotNull(message = "Campo 'name' não pode ser nulo")
    private String text;

    @NotNull(message = "Campo 'email' não pode ser nulo")
    private User user;
}
