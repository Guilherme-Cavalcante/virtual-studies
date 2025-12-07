package br.ifsp.ms_gamification.domain.entities;

import lombok.Data;

@Data
public class Score {

    private Long studentId;
    private int points;

    public Score() {    }

    public Score(Long studentId, int points) {
        this.studentId = studentId;
        this.points = points;
    }

    public void addPoints(int points) {
        if (points < 0 && Math.abs(points) > this.points) {
            this.points = 0;
        } else {
            this.points += points;
        }
    }
}
