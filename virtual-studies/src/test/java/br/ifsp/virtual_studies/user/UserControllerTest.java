package br.ifsp.virtual_studies.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.virtual_studies.controller.UserController;
import br.ifsp.virtual_studies.dto.user.UserRegistrationDTO;
import br.ifsp.virtual_studies.dto.user.UserResponseDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Role;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ChatRepository chatRepository;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@hotmail.com");
        dto.setPassword("John.1234");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()));
    }

    @Test
    void shouldNotCreateUser() throws Exception {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName("");
        dto.setEmail("john.doe@");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindUser() throws Exception {
        Student user = new Student();
        user.setName("John Doe");
        user.setEmail("john.doe@hotmail.com");
        user.setPassword("John.1234");

        Student savedUser = studentRepository.save(user);
        assertNotNull(savedUser);

        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value(savedUser.getName()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()));
    }

    @Test
    void shouldNotFindUser() throws Exception {
        long id = 999L;
        
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListUsersChats() throws Exception {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@gmail.com");
        student.setPassword("john.1234");
        Student savedStudent = studentRepository.save(student);

        Teacher teacher_1 = new Teacher();
        teacher_1.setName("Jane Doe");
        teacher_1.setEmail("jane.doe@hotmail.com");
        teacher_1.setPassword("Bio.1234");
        Teacher savedTeacher_1 = teacherRepository.save(teacher_1);

        Teacher teacher_2 = new Teacher();
        teacher_2.setName("Jill Doe");
        teacher_2.setEmail("jill.doe@outlook.com");
        teacher_2.setPassword("Lit.1234");
        Teacher savedTeacher_2 = teacherRepository.save(teacher_2);

        Chat chat_1 = new Chat();
        chat_1.setSubject("Biologia");
        chat_1.setTeacher(savedTeacher_1);
        Chat savedChat_1 = chatRepository.save(chat_1);
        savedStudent.addToChat(savedChat_1);
        
        Chat chat_2 = new Chat();
        chat_2.setSubject("Literatura");
        chat_2.setTeacher(savedTeacher_2);
        Chat savedChat_2 = chatRepository.save(chat_2);
        savedStudent.addToChat(savedChat_2);
        
        savedStudent = studentRepository.save(student);
        
        mockMvc.perform(get("/api/users/{id}/chats?page=0&size=5", savedStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}
