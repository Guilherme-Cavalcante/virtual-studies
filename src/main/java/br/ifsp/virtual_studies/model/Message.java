package br.ifsp.virtual_studies.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Thanks> thanks = new ArrayList<>();
    
    private LocalDateTime createdAt;

    public boolean studentAlreadyThanked(Student student) {
        for (Thanks thanks : this.thanks) {
            if (student.equals(thanks.getStudent())) {
                return true;
            }
        }
        return false;
    }

    public void addThanks(Thanks thanks) {
        this.thanks.add(thanks);
    }

    public void removeThanks(Thanks thanks) {
        this.thanks.remove(thanks);
    }

    public Thanks removeThanks(Student student) {
        for (Thanks thanks : this.thanks) {
            if (student.equals(thanks.getStudent())) {
                removeThanks(thanks);
                return thanks;
            }
        }
        return null;
    }
}
