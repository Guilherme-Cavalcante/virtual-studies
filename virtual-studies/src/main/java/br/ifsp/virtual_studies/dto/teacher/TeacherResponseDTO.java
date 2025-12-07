package br.ifsp.virtual_studies.dto.teacher;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TeacherResponseDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
