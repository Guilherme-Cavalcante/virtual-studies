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

import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.chat.ChatRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.service.ChatService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @PostMapping
    public ResponseEntity<ChatResponseDTO> createChat(@Valid @RequestBody ChatRequestDTO chat) {
        ChatResponseDTO chatResponseDTO = chatService.createChat(chat);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatResponseDTO);
    }
    
    @GetMapping
    public ResponseEntity<PagedResponse<ChatResponseDTO>> getAllChats(Pageable pageable) {
        return ResponseEntity.ok(chatService.getAllChats(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDTO> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ChatResponseDTO> updateChat(@PathVariable Long id,
            @Valid @RequestBody ChatRequestDTO chatDto) {
        ChatResponseDTO updatedChat = chatService.updateChat(id, chatDto);
        return ResponseEntity.ok(updatedChat);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }
}
