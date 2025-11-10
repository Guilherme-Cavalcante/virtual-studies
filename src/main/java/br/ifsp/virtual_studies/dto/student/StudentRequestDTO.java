package br.ifsp.virtual_studies.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRequestDTO {

    @NotNull(message = "Campo 'name' não pode ser nulo")
    @Size(min=2, max=100)
    private String name;

    @NotNull(message = "Campo 'email' não pode ser nulo")
    @Email
    private String email;

    @NotNull(message = "Campo 'password' não pode ser nulo")
    private String password;
}
