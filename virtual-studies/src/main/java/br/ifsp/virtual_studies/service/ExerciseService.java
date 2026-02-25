package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.exercise.ExercisePatchDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.model.User;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.ExerciseRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    public ExerciseService(ExerciseRepository exerciseRepository, ModelMapper modelMapper,
            StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    public ExerciseResponseDTO getExerciseById(Long idExercise, User user) {
        Student studentPossibleUser = studentRepository.findById(user.getId())
                .orElse(new Student());
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        if (studentPossibleUser.getId() == null && teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise not found with ID: " + idExercise));
        return modelMapper.map(exercise, ExerciseResponseDTO.class);
    }

    public ExerciseResponseDTO updateExercise(Long idExercise,
            ExercisePatchDTO exerciseDto,
            User user) {
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        if (teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        Exercise existingExercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise not found with ID: " + idExercise));
        if (exerciseDto.getTitle().isPresent())
            existingExercise.setTitle(exerciseDto.getTitle().get());
        if (exerciseDto.getDescription().isPresent())
            existingExercise.setDescription(exerciseDto.getDescription().get());
        if (exerciseDto.getLink().isPresent())
            existingExercise.setLink(exerciseDto.getLink().get());
        Exercise updatedExercise = exerciseRepository.save(existingExercise);
        return modelMapper.map(updatedExercise, ExerciseResponseDTO.class);
    }

    public void deleteExercise(Long idExercise, User user) {
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        Exercise exercise = exerciseRepository.findById(idExercise)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise not found with ID: " + idExercise));
        if (teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        exerciseRepository.delete(exercise);
    }
}