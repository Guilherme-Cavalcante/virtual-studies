package br.ifsp.ms_gamification.infraestructure.utils.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.ifsp.ms_gamification.application.dto.score.ScoreResponseDTO;
import br.ifsp.ms_gamification.domain.entities.Score;
import br.ifsp.ms_gamification.infraestructure.persistence.entities.ScoreJpaEntity;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Score, ScoreJpaEntity>() {
            @Override
            protected void configure() {
                map().setStudentId(source.getStudentId());
                map().setPoints(source.getPoints());
            }
        });
        modelMapper.addMappings(new PropertyMap<ScoreJpaEntity, Score>() {
            @Override
            protected void configure() {
                map().setStudentId(source.getStudentId());
                map().setPoints(source.getPoints());
            }
        });
        modelMapper.typeMap(Score.class, ScoreResponseDTO.class).setProvider(req -> {
            Score source = (Score) req.getSource();
            return new ScoreResponseDTO(
                    source.getStudentId(),
                    source.getPoints());
        });

        return modelMapper;
    }
}