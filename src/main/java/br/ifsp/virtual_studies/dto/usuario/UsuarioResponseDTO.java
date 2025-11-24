package br.ifsp.virtual_studies.dto.usuario;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}

