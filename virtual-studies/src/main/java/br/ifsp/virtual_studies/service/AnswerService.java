package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.answer.AnswerRequestDTO;
import br.ifsp.virtual_studies.dto.answer.AnswerResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Answer;
import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.repository.AnswerRepository;
import br.ifsp.virtual_studies.repository.ExerciseRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final ExerciseRepository exerciseRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public AnswerService(AnswerRepository answerRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper, ExerciseRepository exerciseRepository, StudentRepository studentRepository) {
        this.answerRepository = answerRepository;
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public AnswerResponseDTO createAnswer(AnswerRequestDTO answerDto) {
        Answer answer = new Answer();
        Exercise exercise = exerciseRepository.findById(answerDto.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));
        Student student = studentRepository.findById(answerDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        answer.setExercise(exercise);
        answer.setStudent(student);
        answer.setGrade(answerDto.getGrade());
        answer.setCreatedAt(LocalDateTime.now());
        Answer createdAnswer = answerRepository.save(answer);
        return modelMapper.map(createdAnswer, AnswerResponseDTO.class);
    }
    
    public PagedResponse<AnswerResponseDTO> getAllAnswers(Pageable pageable) {
        Page<Answer> answersPage = answerRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(answersPage, AnswerResponseDTO.class);
    }
    
    public AnswerResponseDTO getAnswerById(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Answer not found"));
        return modelMapper.map(answer, AnswerResponseDTO.class);
    }
    
    public AnswerResponseDTO updateAnswer(Long id, AnswerRequestDTO answerDto) {
        
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with ID: " + id));
        
        modelMapper.map(answerDto, existingAnswer);
        existingAnswer.setId(id);
        Answer updatedAnswer = answerRepository.save(existingAnswer);
        return modelMapper.map(updatedAnswer, AnswerResponseDTO.class);
    }
    
    public void deleteAnswer(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found with ID: " + id));
        
        answerRepository.delete(answer);
    }
}