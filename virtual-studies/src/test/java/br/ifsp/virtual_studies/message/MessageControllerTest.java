package br.ifsp.virtual_studies.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.virtual_studies.dto.message.MessagePatchDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void cleanDb() {
        chatRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldDeleteMessage() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@yahoo.com");
        student.setPassword("John.1234");
        Student savedStudent = studentRepository.save(student);

        savedChat.addStudent(savedStudent);
        chatRepository.save(savedChat);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        Message message = new Message();
        message.setAuthor(savedStudent);
        message.setText("lol");
        message.setChat(savedChat);
        messageRepository.save(message);

        savedChat = chatRepository.findById(savedChat.getId()).get();

        Pageable pageable = new Pageable() {
            public int getPageNumber() {
                return 1;
            }

            public int getPageSize() {
                return 5;
            }

            public long getOffset() {
                return 0;
            }

            public Sort getSort() {
                return Sort.unsorted();
            }

            public Pageable next() {
                return this;
            }

            public Pageable previousOrFirst() {
                return this;
            }

            public Pageable first() {
                return this;
            }

            public Pageable withPage(int pageNumber) {
                return this;
            }

            public boolean hasPrevious() {
                return false;
            }
        };

        Message savedMessage = messageRepository.findByChat(savedChat, pageable)
                .toList()
                .getFirst();

        mockMvc.perform(delete("/api/messages/{idMessage}", savedMessage.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldEditMessage() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@yahoo.com");
        student.setPassword("John.1234");
        Student savedStudent = studentRepository.save(student);

        savedChat.addStudent(savedStudent);
        chatRepository.save(savedChat);
        
        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);
        
        Message message = new Message();
        message.setAuthor(savedStudent);
        message.setText("lol");
        message.setChat(savedChat);
        messageRepository.save(message);

        assertEquals(message.getAuthor().getId(), savedStudent.getId());

        MessagePatchDTO dto = new MessagePatchDTO();
        dto.setText(Optional.of("lmao"));

        mockMvc.perform(put("/api/messages/{idMessage}", message.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(dto.getText().get()));
    }

    @Test
    void shouldGetMessageById() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@yahoo.com");
        student.setPassword("John.1234");
        Student savedStudent = studentRepository.save(student);

        savedChat.addStudent(savedStudent);
        chatRepository.save(savedChat);
        
        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);
        
        Message message = new Message();
        message.setAuthor(savedStudent);
        message.setText("lol");
        message.setChat(savedChat);
        messageRepository.save(message);

        assertEquals(message.getAuthor().getId(), savedStudent.getId());

        mockMvc.perform(get("/api/messages/{idMessage}", message.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(message.getText()));
    }
}
