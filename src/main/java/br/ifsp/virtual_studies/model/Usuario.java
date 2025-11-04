package br.ifsp.virtual_studies.model;

import org.springframework.stereotype.Service;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Service
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
