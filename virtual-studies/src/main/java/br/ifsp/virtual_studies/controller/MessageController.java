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
import br.ifsp.virtual_studies.dto.message.MessageRequestDTO;
import br.ifsp.virtual_studies.model.UsuarioAuthenticated;
import br.ifsp.virtual_studies.service.MessageService;
import jakarta.validation.Valid;

@Deprecated
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    
    // @PostMapping
    // public ResponseEntity<MessageResponseDTO> createMessage(@Valid @RequestBody MessageRequestDTO message) {
    //     MessageResponseDTO messageResponseDTO = messageService.createMessage(message);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
    // }
    
    // @GetMapping
    // public ResponseEntity<PagedResponse<MessageResponseDTO>> getAllMessages(Pageable pageable) {
    //     return ResponseEntity.ok(messageService.getAllMessages(pageable));
    // }
    
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable Long id,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        return ResponseEntity.ok(messageService.getMessageById(id, authentication.getUsuario()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateMessage(@PathVariable Long id,
            @Valid @RequestBody MessageRequestDTO messageDto,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MessageResponseDTO updatedMessage = messageService.updateMessage(id, messageDto, authentication.getUsuario());
        return ResponseEntity.ok(updatedMessage);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
