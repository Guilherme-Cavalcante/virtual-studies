package br.ifsp.virtual_studies.model;

import java.util.Arrays;

public enum Role {
    STUDENT, TEACHER;

    public static Role fromString(String value) {
        return Arrays.stream(values()).filter(c -> c.name().equalsIgnoreCase(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + value));
    }
}
