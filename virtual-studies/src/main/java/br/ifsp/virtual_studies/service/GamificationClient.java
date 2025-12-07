package br.ifsp.virtual_studies.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ifsp.virtual_studies.dto.score.ScoreRequestDTO;
import br.ifsp.virtual_studies.dto.score.ScoreResponseDTO;

@Service
public class GamificationClient {

    private final RestTemplate restTemplate;
    private final String GAMIFICATION_URL = "http://localhost:8081/api/scores";

    public GamificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addPoints(Long studentId, int points) {
        ScoreRequestDTO request = new ScoreRequestDTO(studentId, points);
        restTemplate.postForEntity(GAMIFICATION_URL, request, Void.class);
    }

    public ResponseEntity<ScoreResponseDTO> getPoints(Long studentId) {
        ResponseEntity<ScoreResponseDTO> response = restTemplate.getForEntity(GAMIFICATION_URL, ScoreResponseDTO.class);
        // ScoreResponseDTO scoreDto = response.getBody();
        // return scoreDto;
        return response;
    }
}
