package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.chat.*;
import br.ifsp.virtual_studies.dto.exercise.*;
import br.ifsp.virtual_studies.dto.material.*;
import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
import br.ifsp.virtual_studies.dto.message.*;
import br.ifsp.virtual_studies.dto.answer.*;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.*;
import br.ifsp.virtual_studies.dto.thanks.*;
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "Operations relationated with the materials, exercises and messages sending area")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(summary = "Create new chat")
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ChatResponseDTO> createChat(@Valid @RequestBody ChatRequestDTO chat,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        ChatResponseDTO chatResponseDTO = chatService.createChat(chat, authentication.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatResponseDTO);
    }

    @Operation(summary = "List teacher chats")
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<PagedResponse<ChatResponseDTO>> listTeacherChats(
            @AuthenticationPrincipal UserAuthenticated authentication,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getTeacherChats(authentication.getUser(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponseDTO> getChatById(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatById(id));
    }

    @Operation(summary = "Update chat")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ChatResponseDTO> updateChat(@PathVariable Long id,
            @Valid @RequestBody ChatPatchDTO chatDto) {
        ChatResponseDTO updatedChat = chatService.updateChat(id, chatDto);
        return ResponseEntity.ok(updatedChat);
    }

    @Operation(summary = "Add student to the chat or remove an already added one")
    @PostMapping("/{idChat}/students/{idStudent}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<PagedResponse<StudentResponseDTO>> assignOrUnassignStudentToChat(@PathVariable Long idChat,
            @PathVariable Long idStudent,
            @AuthenticationPrincipal UserAuthenticated authentication, Pageable pageable) {
        PagedResponse<StudentResponseDTO> chatStudents = chatService.assignOrUnassignStudent(idChat, idStudent,
                authentication.getUser(), pageable);
        return ResponseEntity.ok(chatStudents);
    }

    @Operation(summary = "Delete chat")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        chatService.deleteChat(id, authentication.getUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Send message to the chat")
    @PostMapping("/{idChat}/messages")
    public ResponseEntity<PagedResponse<MessageResponseDTO>> createMessage(@PathVariable Long idChat,
            @Valid @RequestBody MessageRequestDTO message,
            @AuthenticationPrincipal UserAuthenticated authentication, Pageable pageable) {
        PagedResponse<MessageResponseDTO> chatMessages = chatService.createMessage(idChat, message,
                authentication.getUser(), pageable);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatMessages);
    }

    @Operation(summary = "List chat messages")
    @GetMapping("/{idChat}/messages")
    public ResponseEntity<PagedResponse<MessageResponseDTO>> listChatMessages(@PathVariable Long idChat,
            @AuthenticationPrincipal UserAuthenticated authentication,
            Pageable pageable) {
        return ResponseEntity.ok(chatService.getMessages(idChat, authentication.getUser(), pageable));
    }

    @Operation(summary = "Send material")
    @PostMapping("/{idChat}/materials")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResponseDTO> createMaterial(@PathVariable Long idChat,
            @Valid @RequestBody MaterialRequestDTO material,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MaterialResponseDTO newMaterial = chatService.createMaterial(idChat, material, authentication.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(newMaterial);
    }

    @Operation(summary = "List materials")
    @GetMapping("/{idChat}/materials")
    public ResponseEntity<PagedResponse<MaterialResponseDTO>> listMaterials(@PathVariable Long idChat,
            Pageable pageable,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(chatService.listMaterials(idChat, authentication.getUser(), pageable));
    }

    @Operation(summary = "Send exercise")
    @PostMapping("/{idChat}/exercises")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExerciseResponseDTO> createExercise(@PathVariable Long idChat,
            @Valid @RequestBody ExerciseRequestDTO exercise,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        ExerciseResponseDTO newExercise = chatService.createExercise(idChat, exercise, authentication.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(newExercise);
    }

    @Operation(summary = "List exercises")
    @GetMapping("/{idChat}/exercises")
    public ResponseEntity<PagedResponse<ExerciseResponseDTO>> listExercises(@PathVariable Long idChat,
            Pageable pageable,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(chatService.listExercises(idChat, authentication.getUser(), pageable));
    }

    @Operation(summary = "Schedule meeting")
    @PostMapping("/{idChat}/meetings")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MeetingResponseDTO> createMeeting(@PathVariable Long idChat,
            @Valid @RequestBody MeetingRequestDTO meeting,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MeetingResponseDTO newMeeting = chatService.createMeeting(idChat, meeting, authentication.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(newMeeting);
    }

    @Operation(summary = "List scheduled meetings")
    @GetMapping("/{idChat}/meetings")
    public ResponseEntity<PagedResponse<MeetingResponseDTO>> listMeetings(@PathVariable Long idChat,
            Pageable pageable,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(chatService.listMeetings(idChat, authentication.getUser(), pageable));
    }
}
