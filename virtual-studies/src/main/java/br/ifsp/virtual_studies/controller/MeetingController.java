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

import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingPatchDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.model.UserAuthenticated;
import br.ifsp.virtual_studies.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meetings")
@Tag(name = "Meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Operation(summary = "Fetch meeting")
    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> getMeetingById(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        return ResponseEntity.ok(meetingService.getMeetingById(id, authentication.getUser()));
    }

    @Operation(summary = "Update meeting")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MeetingResponseDTO> updateMeeting(@PathVariable Long id,
            @Valid @RequestBody MeetingPatchDTO meetingDto,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        MeetingResponseDTO updatedMeeting = meetingService.updateMeeting(id, meetingDto, authentication.getUser());
        return ResponseEntity.ok(updatedMeeting);
    }

    @Operation(summary = "Delete meeting")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticated authentication) {
        meetingService.deleteMeeting(id, authentication.getUser());
        return ResponseEntity.noContent().build();
    }
}
