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

import br.ifsp.virtual_studies.dto.chat.*;
import br.ifsp.virtual_studies.dto.exercise.*;
import br.ifsp.virtual_studies.dto.material.*;
import br.ifsp.virtual_studies.dto.message.*;
import br.ifsp.virtual_studies.dto.answer.*;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.*;
import br.ifsp.virtual_studies.dto.thanks.*;
import br.ifsp.virtual_studies.model.UsuarioAuthenticated;
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ChatResponseDTO> createChat(@Valid @RequestBody ChatRequestDTO chat,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        ChatResponseDTO chatResponseDTO = chatService.createChat(chat, authentication.getUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatResponseDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<PagedResponse<ChatResponseDTO>> listTeacherChats(
            @AuthenticationPrincipal UsuarioAuthenticated authentication,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getTeacherChats(authentication.getUsuario(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDTO> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ChatResponseDTO> updateChat(@PathVariable Long id,
            @Valid @RequestBody ChatPatchDTO chatDto) {
        ChatResponseDTO updatedChat = chatService.updateChat(id, chatDto);
        return ResponseEntity.ok(updatedChat);
    }

    @PostMapping("/{idChat}/students/{idStudent}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ChatResponseDTO> assignOrUnassignStudentToChat(@PathVariable Long idChat,
            @PathVariable Long idStudent,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        ChatResponseDTO chat = chatService.assignOrUnassignStudent(idChat, idStudent, authentication.getUsuario());
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        chatService.deleteChat(id, authentication.getUsuario());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idChat}/messages")
    public ResponseEntity<MessageResponseDTO> createOrDeleteMessage(@PathVariable Long idChat,
            @Valid @RequestBody MessageRequestDTO message,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MessageResponseDTO newMessage = chatService.createOrDeleteMessage(idChat, message, authentication.getUsuario());
        return ResponseEntity.ok(newMessage);
    }

    @GetMapping("/{idChat}/messages")
    public ResponseEntity<PagedResponse<MessageResponseDTO>> listChatMessages(@PathVariable Long idChat,
            @AuthenticationPrincipal UsuarioAuthenticated authentication,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getMessages(idChat, authentication.getUsuario(), pageable));
    }

    @PutMapping("/{idChat}/messages/{idMessage}/thanks")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<MessageResponseDTO> thankOrUnthankMessage(@PathVariable Long idChat,
            @PathVariable Long idMessage,
            @Valid @RequestBody ThanksPatchDTO thanks,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MessageResponseDTO updatedMessage = chatService.thankOrUnthankMessage(idChat, idMessage, thanks,
                authentication.getUsuario());
        return ResponseEntity.ok(updatedMessage);
    }

    @PostMapping("/{idChat}/materials")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponseDTO> createMaterial(@PathVariable Long idChat,
            @Valid @RequestBody MaterialRequestDTO material,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MaterialResponseDTO newMaterial = chatService.createMaterial(idChat, material, authentication.getUsuario());
        return ResponseEntity.ok(newMaterial);
    }

    @GetMapping("/{idChat}/materials")
    public ResponseEntity<PagedResponse<MaterialResponseDTO>> listMaterials(@PathVariable Long idChat,
            Pageable pageable,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        return ResponseEntity.ok(chatService.listMaterials(idChat, authentication.getUsuario(), pageable));
    }

    @GetMapping("/{idChat}/materials/{idMaterial}")
    public ResponseEntity<MaterialResponseDTO> getMaterialById(@PathVariable Long idChat, @PathVariable Long idMaterial,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MaterialResponseDTO newMaterial = chatService.getMaterial(idChat, idMaterial, authentication.getUsuario());
        return ResponseEntity.ok(newMaterial);
    }

    @PutMapping("/{idChat}/materials/{idMaterial}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponseDTO> updateMaterial(@PathVariable Long idChat, @PathVariable Long idMaterial,
            @Valid @RequestBody MaterialPatchDTO material,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        MaterialResponseDTO newMaterial = chatService.updateMaterial(idChat, idMaterial, material,
                authentication.getUsuario());
        return ResponseEntity.ok(newMaterial);
    }

    @DeleteMapping("/{idChat}/materials/{idMaterial}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponseDTO> deleteMaterial(@PathVariable Long idChat, @PathVariable Long idMaterial,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        chatService.deleteMaterial(idChat, idMaterial, authentication.getUsuario());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idChat}/exercises")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExerciseResponseDTO> createExercise(@PathVariable Long idChat,
            @Valid @RequestBody ExerciseRequestDTO exercise,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        ExerciseResponseDTO newExercise = chatService.createExercise(idChat, exercise, authentication.getUsuario());
        return ResponseEntity.ok(newExercise);
    }

    @GetMapping("/{idChat}/exercises")
    public ResponseEntity<PagedResponse<ExerciseResponseDTO>> listExercises(@PathVariable Long idChat,
            Pageable pageable,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        return ResponseEntity.ok(chatService.listExercises(idChat, authentication.getUsuario(), pageable));
    }

    @GetMapping("/{idChat}/exercises/{idExercise}")
    public ResponseEntity<ExerciseResponseDTO> getExerciseById(@PathVariable Long idChat, @PathVariable Long idExercise,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        ExerciseResponseDTO newExercise = chatService.getExercise(idChat, idExercise, authentication.getUsuario());
        return ResponseEntity.ok(newExercise);
    }

    @PutMapping("/{idChat}/exercises/{idExercise}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(@PathVariable Long idChat, @PathVariable Long idExercise,
            @Valid @RequestBody ExercisePatchDTO exercise,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        ExerciseResponseDTO newExercise = chatService.updateExercise(idChat, idExercise, exercise,
                authentication.getUsuario());
        return ResponseEntity.ok(newExercise);
    }

    @DeleteMapping("/{idChat}/exercises/{idExercise}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExerciseResponseDTO> deleteExercise(@PathVariable Long idChat, @PathVariable Long idExercise,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        chatService.deleteExercise(idChat, idExercise, authentication.getUsuario());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idChat}/exercises/{idExercise}/answers")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AnswerResponseDTO> sendAnswer(@PathVariable Long idChat, @PathVariable Long idExercise,
            @Valid @RequestBody AnswerRequestDTO answer,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        AnswerResponseDTO newAnswer = chatService.createAnswer(idChat, idExercise, answer, authentication.getUsuario());
        return ResponseEntity.status(HttpStatus.CREATED).body(newAnswer);
    }

    @PutMapping("/{idChat}/exercises/{idExercise}/answers/{idAnswer}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AnswerResponseDTO> updateAnswer(@PathVariable Long idChat, @PathVariable Long idExercise, @PathVariable Long idAnswer, @Valid @RequestBody AnswerPatchDTO answer,
            @AuthenticationPrincipal UsuarioAuthenticated authentication) {
        AnswerResponseDTO updatedAnswer = chatService.updateAnswer(idChat, idExercise, idAnswer, answer, authentication.getUsuario());
        return ResponseEntity.ok(updatedAnswer);
    }
}
