// package br.ifsp.virtual_studies.controller;

// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
// import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
// import br.ifsp.virtual_studies.dto.page.PagedResponse;
// import br.ifsp.virtual_studies.service.MeetingService;
// import jakarta.validation.Valid;

// @RestController
// @RequestMapping("/api/meetings")
// public class MeetingController {
//     private final MeetingService meetingService;
    
//     public MeetingController(MeetingService meetingService) {
//         this.meetingService = meetingService;
//     }
    
//     @PostMapping
//     public ResponseEntity<MeetingResponseDTO> createMeeting(@Valid @RequestBody MeetingRequestDTO meeting) {
//         MeetingResponseDTO meetingResponseDTO = meetingService.createMeeting(meeting);
//         return ResponseEntity.status(HttpStatus.CREATED).body(meetingResponseDTO);
//     }
    
//     @GetMapping
//     public ResponseEntity<PagedResponse<MeetingResponseDTO>> getAllMeetings(Pageable pageable) {
//         return ResponseEntity.ok(meetingService.getAllMeetings(pageable));
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<MeetingResponseDTO> getMeetingById(@PathVariable Long id) {
//         return ResponseEntity.ok(meetingService.getMeetingById(id));
//     }
    
//     @PutMapping("/{id}")
//     public ResponseEntity<MeetingResponseDTO> updateMeeting(@PathVariable Long id,
//             @Valid @RequestBody MeetingRequestDTO meetingDto) {
//         MeetingResponseDTO updatedMeeting = meetingService.updateMeeting(id, meetingDto);
//         return ResponseEntity.ok(updatedMeeting);
//     }
    
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
//         meetingService.deleteMeeting(id);
//         return ResponseEntity.noContent().build();
//     }
// }
