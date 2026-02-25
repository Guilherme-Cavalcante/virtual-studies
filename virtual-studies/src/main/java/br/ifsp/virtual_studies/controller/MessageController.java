package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.message.MessageResponseDTO;
import br.ifsp.virtual_studies.dto.message.MessagePatchDTO;
import br.ifsp.virtual_studies.dto.message.MessageRequestDTO;
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages")
public class MessageController {
    private final MessageService messageService;
    
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    @Operation(summary = "Fetch message")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(messageService.getMessageById(id, authentication.getUser()));
    }
    
    @Operation(summary = "Editar message")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateMessage(@PathVariable Long id,
            @Valid @RequestBody MessagePatchDTO messageDto,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MessageResponseDTO updatedMessage = messageService.updateMessage(id, messageDto, authentication.getUser());
        return ResponseEntity.ok(updatedMessage);
    }
    
    @Operation(summary = "Delete message")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
