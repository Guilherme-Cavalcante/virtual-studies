package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.exercise.ExerciseRequestDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.ExerciseRepository;
import jakarta.validation.Valid;

@Service
// @Validated
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public ExerciseService(ExerciseRepository exerciseRepository, ChatRepository chatRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.chatRepository = chatRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseDto) {
        Chat chat = chatRepository.findById(exerciseDto.getChatId())
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        Exercise exercise = new Exercise();
        exercise.setTitle(exerciseDto.getTitle());
        exercise.setDescription(exerciseDto.getDescription());
        exercise.setLink(exerciseDto.getLink());
        exercise.setChat(chat);
        exercise.setCreatedAt(LocalDateTime.now());
        Exercise createdExercise = exerciseRepository.save(exercise);
        return modelMapper.map(createdExercise, ExerciseResponseDTO.class);
    }
    
    public PagedResponse<ExerciseResponseDTO> getAllExercises(Pageable pageable) {
        Page<Exercise> exercisesPage = exerciseRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(exercisesPage, ExerciseResponseDTO.class);
    }
    
    public ExerciseResponseDTO getExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));
        return modelMapper.map(exercise, ExerciseResponseDTO.class);
    }
    
    public ExerciseResponseDTO updateExercise(Long id, ExerciseRequestDTO exerciseDto) {
        
        Exercise existingExercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + id));
        
        modelMapper.map(exerciseDto, existingExercise);
        existingExercise.setId(id);
        Exercise updatedExercise = exerciseRepository.save(existingExercise);
        return modelMapper.map(updatedExercise, ExerciseResponseDTO.class);
    }
    
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + id));
        
        exerciseRepository.delete(exercise);
    }
}