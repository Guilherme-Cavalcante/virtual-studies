package br.ifsp.virtual_studies.dto.student;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
