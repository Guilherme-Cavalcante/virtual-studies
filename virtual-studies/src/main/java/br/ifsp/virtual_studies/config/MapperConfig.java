package br.ifsp.virtual_studies.config;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.ifsp.virtual_studies.dto.answer.AnswerResponseDTO;
import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseResponseDTO;
import br.ifsp.virtual_studies.dto.material.MaterialResponseDTO;
import br.ifsp.virtual_studies.dto.message.MessagePatchDTO;
import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksResponseDTO;
import br.ifsp.virtual_studies.dto.user.UserRegistrationDTO;
import br.ifsp.virtual_studies.model.*;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Answer, AnswerResponseDTO>() {
            @Override
            protected void configure() {
                map().setExerciseId(source.getExercise().getId());
                map().setStudentId(source.getStudent().getId());
            }
        });
        modelMapper.addMappings(new PropertyMap<Exercise, ExerciseResponseDTO>() {
            @Override
            protected void configure() {
                map().setChatId(source.getChat().getId());
            }
        });
        modelMapper.addMappings(new PropertyMap<Material, MaterialResponseDTO>() {
            @Override
            protected void configure() {
                map().setChatId(source.getChat().getId());
            }
        });
        modelMapper.addMappings(new PropertyMap<Meeting, MeetingResponseDTO>() {
            @Override
            protected void configure() {
                map().setChatId(source.getChat().getId());
            }
        });
        modelMapper.addMappings(new PropertyMap<Thanks, ThanksResponseDTO>() {

            @Override
            protected void configure() {
                map().setMessageId(source.getMessage().getId());
                map().setStudentId(source.getStudent().getId());
            }
        });
        modelMapper.addMappings(new PropertyMap<UserRegistrationDTO, Student>() {
            
            @Override
            protected void configure() {
                map().setCreatedAt(LocalDateTime.now());
                map().setRole(Role.STUDENT);
            }
        });
        modelMapper.addMappings(new PropertyMap<UserRegistrationDTO, Teacher>() {
            
            @Override
            protected void configure() {
                map().setCreatedAt(LocalDateTime.now());
                map().setRole(Role.TEACHER);
            }
        });
        return modelMapper;
    }
}