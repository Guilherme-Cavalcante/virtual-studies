package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.dto.thanks.ThanksPatchDTO;
import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.service.ThanksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/thanks")
@Tag(name = "Thanks")
public class ThanksController {
    private final ThanksService thanksService;

    public ThanksController(ThanksService thanksService) {
        this.thanksService = thanksService;
    }

    @Operation(summary = "Agradecer exercisem")
    @PostMapping("/{idMessage}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<MessageResponseDTO> thankOrUnthankMessage(@PathVariable Long idMessage,
            @Valid @RequestBody ThanksPatchDTO thanks,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MessageResponseDTO updatedMessage = thanksService.thankOrUnthankMessage(idMessage, thanks,
                authentication.getUser());
        return ResponseEntity.ok(updatedMessage);
    }
}
