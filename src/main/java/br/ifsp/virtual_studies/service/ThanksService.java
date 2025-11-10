package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Thanks;
import br.ifsp.virtual_studies.repository.ThanksRepository;

@Service
public class ThanksService {
    private final ThanksRepository thanksRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public ThanksService(ThanksRepository thanksRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.thanksRepository = thanksRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public ThanksResponseDTO createThanks(ThanksRequestDTO thanksDto) {
        Thanks thanks = modelMapper.map(thanksDto, Thanks.class);
        Thanks createdThanks = thanksRepository.save(thanks);
        return modelMapper.map(createdThanks, ThanksResponseDTO.class);
    }
    
    public PagedResponse<ThanksResponseDTO> getAllThankss(Pageable pageable) {
        Page<Thanks> thankssPage = thanksRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(thankssPage, ThanksResponseDTO.class);
    }
    
    public ThanksResponseDTO getThanksById(Long id) {
        Thanks thanks = thanksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thanks not found"));
        return modelMapper.map(thanks, ThanksResponseDTO.class);
    }
    
    public ThanksResponseDTO updateThanks(Long id, ThanksRequestDTO thanksDto) {
        
        Thanks existingThanks = thanksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thanks not found with ID: " + id));
        
        modelMapper.map(thanksDto, existingThanks);
        existingThanks.setId(id);
        Thanks updatedThanks = thanksRepository.save(existingThanks);
        return modelMapper.map(updatedThanks, ThanksResponseDTO.class);
    }
    
    public void deleteThanks(Long id) {
        Thanks thanks = thanksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thanks not found with ID: " + id));
        
        thanksRepository.delete(thanks);
    }
}