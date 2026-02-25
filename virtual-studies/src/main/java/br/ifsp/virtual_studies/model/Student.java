package br.ifsp.virtual_studies.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// @NoArgsConstructor
@Entity
public class Student extends User {

    @ManyToMany
    @JoinTable
    private Set<Chat> chats = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Student() {
        super();
        this.role = Role.STUDENT;
    }

    public boolean containsChat(Chat chat) {
        return this.chats.contains(chat);
    }

    public void addToChat(Chat chat) {
        this.chats.add(chat);
    }

    public void removeFromChat(Chat chat) {
        this.chats.remove(chat);
    }
}
