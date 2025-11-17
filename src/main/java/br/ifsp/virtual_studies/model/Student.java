package br.ifsp.virtual_studies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// @NoArgsConstructor
@Entity
public class Student extends Usuario {

    private int score;

    @ManyToMany
    @JoinTable
    private List<Chat> chats;

    public Student() {
        super();
    }

    public void updateScore(int value) {
        this.score += value;
    }
}
