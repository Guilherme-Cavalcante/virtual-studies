package br.ifsp.ms_gamification.infraestructure.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ScoreJpaEntity {

    @Id
    private Long studentId;
    private int points;
}
