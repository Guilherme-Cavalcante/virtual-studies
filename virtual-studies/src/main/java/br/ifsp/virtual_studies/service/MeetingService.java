// package br.ifsp.virtual_studies.service;

// import org.modelmapper.ModelMapper;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;

// import br.ifsp.virtual_studies.dto.page.PagedResponse;
// import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
// import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
// import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
// import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
// import br.ifsp.virtual_studies.model.Meeting;
// import br.ifsp.virtual_studies.repository.MeetingRepository;

// @Service
// public class MeetingService {
//     private final MeetingRepository meetingRepository;
//     private final ModelMapper modelMapper;
//     private final PagedResponseMapper pagedResponseMapper;
    
//     public MeetingService(MeetingRepository meetingRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
//         this.meetingRepository = meetingRepository;
//         this.modelMapper = modelMapper;
//         this.pagedResponseMapper = pagedResponseMapper;
//     }
    
//     public MeetingResponseDTO createMeeting(MeetingRequestDTO meetingDto) {
//         Meeting meeting = modelMapper.map(meetingDto, Meeting.class);
//         Meeting createdMeeting = meetingRepository.save(meeting);
//         return modelMapper.map(createdMeeting, MeetingResponseDTO.class);
//     }
    
//     public PagedResponse<MeetingResponseDTO> getAllMeetings(Pageable pageable) {
//         Page<Meeting> meetingsPage = meetingRepository.findAll(pageable);
//         return pagedResponseMapper.toPagedResponse(meetingsPage, MeetingResponseDTO.class);
//     }
    
//     public MeetingResponseDTO getMeetingById(Long id) {
//         Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meeting not found"));
//         return modelMapper.map(meeting, MeetingResponseDTO.class);
//     }
    
//     public MeetingResponseDTO updateMeeting(Long id, MeetingRequestDTO meetingDto) {
        
//         Meeting existingMeeting = meetingRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + id));
        
//         modelMapper.map(meetingDto, existingMeeting);
//         existingMeeting.setId(id);
//         Meeting updatedMeeting = meetingRepository.save(existingMeeting);
//         return modelMapper.map(updatedMeeting, MeetingResponseDTO.class);
//     }
    
//     public void deleteMeeting(Long id) {
//         Meeting meeting = meetingRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with ID: " + id));
        
//         meetingRepository.delete(meeting);
//     }
// }