package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.StudentResponseDTO;
import br.ifsp.virtual_studies.dto.user.UserRegistrationDTO;
import br.ifsp.virtual_studies.dto.user.UserResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.model.User;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.repository.UserRepository;

@Service
public class UserService {
    private final ChatRepository chatRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;

    public UserService(ChatRepository chatRepository, StudentRepository studentRepository, TeacherRepository teacherRepository,
            ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.chatRepository = chatRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }

    public UserResponseDTO createUser(UserRegistrationDTO userDto) {
        if (userDto.isTeacher()) {
            Teacher teacher = modelMapper.map(userDto, Teacher.class);
            Teacher createdTeacher = teacherRepository.save(teacher);
            return modelMapper.map(createdTeacher, UserResponseDTO.class);
        }
        Student student = modelMapper.map(userDto, Student.class);
        Student createdStudent = studentRepository.save(student);
        return modelMapper.map(createdStudent, UserResponseDTO.class);
    }

    public UserResponseDTO getUserById(long id) {
        Teacher teacher = teacherRepository.findById(id)
            .orElse(null);
        Student student = studentRepository.findById(id)
            .orElse(null);
        if (teacher != null) {
            return modelMapper.map(teacher, UserResponseDTO.class);
        }
        if (student != null) {
            return modelMapper.map(student, UserResponseDTO.class);
        }
        throw new ResourceNotFoundException("User not found with id: " + id);
    }
    
    public PagedResponse<ChatResponseDTO> getChats(Long id, Pageable pageable) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Page<Chat> chatsPage = chatRepository.findByStudentsContaining(student, pageable);
        return pagedResponseMapper.toPagedResponse(chatsPage, ChatResponseDTO.class);
    }
}
