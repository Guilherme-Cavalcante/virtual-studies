package br.ifsp.virtual_studies.service;

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
import br.ifsp.virtual_studies.repository.AnswerRepository;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public AnswerService(AnswerRepository answerRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public AnswerResponseDTO createAnswer(AnswerRequestDTO answerDto) {
        Answer answer = modelMapper.map(answerDto, Answer.class);
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