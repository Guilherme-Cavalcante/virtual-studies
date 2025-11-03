package br.ifsp.virtual_studies.dto.teacher;

import lombok.Data;

@Data
public class TeacherResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
}
