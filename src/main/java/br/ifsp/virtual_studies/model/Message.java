package br.ifsp.virtual_studies.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Usuario author;

    @NotBlank
    @Size(max=1000)
    private String text;
    
    private LocalDateTime createdAt;
}
