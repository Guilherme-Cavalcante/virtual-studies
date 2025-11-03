package br.ifsp.virtual_studies.dto.student;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentRequestDTO {

    @NotNull(message = "Campo 'name' não pode ser nulo")
    private String name;

    @NotNull(message = "Campo 'email' não pode ser nulo")
    private String email;

    @NotNull(message = "Campo 'password' não pode ser nulo")
    private String password;
}
