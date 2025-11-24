package br.ifsp.virtual_studies.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Exercise implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=120)
    private String title;

    @NotNull
    @Size(max=500)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @NotBlank
    private String link;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();;

    private LocalDateTime createdAt;

    public boolean hasStudentAlreadyAnswered(Student student) {
        for (Answer answer : this.answers) {
            if (student.equals(answer.getStudent())) {
                return true;
            }
        }
        return false;
    }
}
