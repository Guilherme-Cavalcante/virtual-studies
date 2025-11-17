package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.thanks.ThanksResponseDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.service.ThanksService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/thanks")
public class ThanksController {
    private final ThanksService thanksService;
    
    public ThanksController(ThanksService thanksService) {
        this.thanksService = thanksService;
    }
    
    @PostMapping
    public ResponseEntity<ThanksResponseDTO> createThanks(@Valid @RequestBody ThanksRequestDTO thanks) {
        ThanksResponseDTO thanksResponseDTO = thanksService.createThanks(thanks);
        return ResponseEntity.status(HttpStatus.CREATED).body(thanksResponseDTO);
    }
    
    @GetMapping
    public ResponseEntity<PagedResponse<ThanksResponseDTO>> getAllThankss(Pageable pageable) {
        return ResponseEntity.ok(thanksService.getAllThankss(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ThanksResponseDTO> getThanksById(@PathVariable Long id) {
        return ResponseEntity.ok(thanksService.getThanksById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ThanksResponseDTO> updateThanks(@PathVariable Long id,
            @Valid @RequestBody ThanksRequestDTO thanksDto) {
        ThanksResponseDTO updatedThanks = thanksService.updateThanks(id, thanksDto);
        return ResponseEntity.ok(updatedThanks);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThanks(@PathVariable Long id) {
        thanksService.deleteThanks(id);
        return ResponseEntity.noContent().build();
    }
}
