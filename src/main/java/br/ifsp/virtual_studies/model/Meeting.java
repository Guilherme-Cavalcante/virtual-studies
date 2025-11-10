package br.ifsp.virtual_studies.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Service
public class Meeting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min=3, max=120)
    private String title;

    @NotNull
    @Size(max=500)
    private String description;

    @NotBlank
    @ManyToOne
    private Chat chat;

    private String link;

    private LocalDateTime date;
    
    private boolean closed = false;

    private LocalDateTime createdAt;
}
