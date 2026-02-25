package br.ifsp.virtual_studies.thanks;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.virtual_studies.dto.thanks.ThanksRequestDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.repository.ThanksRepository;
import br.ifsp.virtual_studies.service.GamificationClient;
import br.ifsp.virtual_studies.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ThanksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ThanksRepository thanksRepository;

    @Autowired
    private GamificationClient gamificationClient;

    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void cleanDb() {
        chatRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        thanksRepository.deleteAll();
    }

    @Test
    void shouldThankAndUnthankMessage() throws JsonProcessingException, Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);
        
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@yahoo.com");
        student.setPassword("John.1234");
        Student savedStudent = studentRepository.save(student);

        savedStudent.addToChat(savedChat);
        savedStudent = studentRepository.save(savedStudent);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        Message message = new Message();
        message.setAuthor(savedStudent);
        message.setChat(savedChat);
        message.setText("lol");

        Student thanker = new Student();
        thanker.setName("Peter Doe");
        thanker.setEmail("peter.doe@yahoo.com");
        thanker.setPassword("Peter.1234");
        Student savedThanker = studentRepository.save(thanker);

        savedThanker.addToChat(savedChat);
        savedThanker = studentRepository.save(savedThanker);

        Message savedMessage = messageRepository.save(message);

        ThanksRequestDTO dto = new ThanksRequestDTO();
        dto.setMessage("Obrigado");
        dto.setStudentId(savedThanker.getId());

        mockMvc.perform(post("/api/thanks/{idMessage}", savedMessage.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(savedMessage.getText()));
    }
}
