package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.meeting.MeetingPatchDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.model.User;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MeetingRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    public MeetingService(MeetingRepository meetingRepository, ModelMapper modelMapper,
            StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.meetingRepository = meetingRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
    }

    public MeetingResponseDTO getMeetingById(Long idMeeting, User user) {
        Student studentPossibleUser = studentRepository.findById(user.getId())
                .orElse(new Student());
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        if (studentPossibleUser.getId() == null && teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting not found with ID: " + idMeeting));
        return modelMapper.map(meeting, MeetingResponseDTO.class);
    }

    public MeetingResponseDTO updateMeeting(Long idMeeting,
            MeetingPatchDTO meetingDto,
            User user) {
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        if (teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        Meeting existingMeeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting not found with ID: " + idMeeting));
        if (meetingDto.getTitle().isPresent())
            existingMeeting.setTitle(meetingDto.getTitle().get());
        if (meetingDto.getDescription().isPresent())
            existingMeeting.setDescription(meetingDto.getDescription().get());
        if (meetingDto.getLink().isPresent())
            existingMeeting.setLink(meetingDto.getLink().get());
        Meeting updatedMeeting = meetingRepository.save(existingMeeting);
        return modelMapper.map(updatedMeeting, MeetingResponseDTO.class);
    }

    public void deleteMeeting(Long idMeeting, User user) {
        Teacher teacherPossibleUser = teacherRepository.findById(user.getId())
                .orElse(new Teacher());
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meeting not found with ID: " + idMeeting));
        if (teacherPossibleUser.getId() == null) {
            throw new AccessDeniedException("Access Denied");
        }
        meetingRepository.delete(meeting);
    }
}