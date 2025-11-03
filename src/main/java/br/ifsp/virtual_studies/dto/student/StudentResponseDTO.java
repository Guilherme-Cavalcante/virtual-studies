package br.ifsp.virtual_studies.dto.student;

import lombok.Data;

@Data
public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
}
