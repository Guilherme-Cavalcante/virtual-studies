package br.ifsp.virtual_studies.chat;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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

import br.ifsp.virtual_studies.dto.chat.ChatRequestDTO;
import br.ifsp.virtual_studies.dto.exercise.ExerciseRequestDTO;
import br.ifsp.virtual_studies.dto.material.MaterialRequestDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingRequestDTO;
import br.ifsp.virtual_studies.dto.message.MessageRequestDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Exercise;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Message;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.ExerciseRepository;
import br.ifsp.virtual_studies.repository.MaterialRepository;
import br.ifsp.virtual_studies.repository.MeetingRepository;
import br.ifsp.virtual_studies.repository.MessageRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void cleanDb() {
        chatRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldCreateChat() throws Exception {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setSubject("Biologia");

        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@hotmail.com");
        teacher.setPassword("Bio.1234");

        Teacher savedTeacher = teacherRepository.save(teacher);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        mockMvc.perform(post("/api/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subject").value(dto.getSubject()));
    }

    @Test
    void shouldFailCreationByAuthentication() throws Exception {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setSubject("Biologia");

        mockMvc.perform(post("/api/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailCreationByWrongRole() throws Exception {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setSubject("Biologia");

        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@gmail.com");
        student.setPassword("John.1234");

        Student savedStudent = studentRepository.save(student);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        mockMvc.perform(post("/api/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAssignAndUnassignStudent() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@hotmail.com");
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

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        mockMvc.perform(post("/api/chats/{idChat}/students/{idStudent}", savedChat.getId(), savedStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        mockMvc.perform(post("/api/chats/{idChat}/students/{idStudent}", savedChat.getId(), savedStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void deleteChat() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@hotmail.com");
        teacher.setPassword("Bio.1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        mockMvc.perform(delete("/api/chats/{idChat}", savedChat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateMessage() throws Exception {
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

        savedStudent.addToChat(savedChat);
        savedStudent = studentRepository.save(savedStudent);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setText("lol");

        mockMvc.perform(post("/api/chats/{idChat}/messages", savedChat.getId(), savedStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void shouldCreateMaterial() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        MaterialRequestDTO dto = new MaterialRequestDTO();
        dto.setTitle("Aula de Biologia");
        dto.setDescription("Assistam");
        dto.setLocal("example.drive.com");

        mockMvc.perform(post("/api/chats/{idChat}/materials", savedChat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(dto.getTitle()));
    }

    @Test
    void shouldListMaterials() throws Exception {
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

        Material material_1 = new Material();
        material_1.setTitle("Aula de Biologia");
        material_1.setDescription("Assistam");
        material_1.setLocal("example.drive.com");
        material_1.setChat(savedChat);
        materialRepository.save(material_1);

        Material material_2 = new Material();
        material_2.setTitle("Artigo de Biologia");
        material_2.setDescription("Leiam");
        material_2.setLocal("example.drive.com");
        material_2.setChat(savedChat);
        materialRepository.save(material_2);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        mockMvc.perform(get("/api/chats/{id}/materials?page=0&size=5", savedChat.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
    
    @Test
    void shouldCreateExercise() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        ExerciseRequestDTO dto = new ExerciseRequestDTO();
        dto.setTitle("Atividades de Biologia");
        dto.setDescription("Facam");
        dto.setLink("example.drive.com");

        mockMvc.perform(post("/api/chats/{idChat}/exercises", savedChat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(dto.getTitle()));
    }

    @Test
    void shouldListExercises() throws Exception {
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

        Exercise exercise_1 = new Exercise();
        exercise_1.setTitle("Atividades de Biologia");
        exercise_1.setDescription("Facam");
        exercise_1.setLink("example.drive.com");
        exercise_1.setChat(savedChat);
        exerciseRepository.save(exercise_1);

        Exercise exercise_2 = new Exercise();
        exercise_2.setTitle("Revisao de Biologia");
        exercise_2.setDescription("Relembrem");
        exercise_2.setLink("example.drive.com");
        exercise_2.setChat(savedChat);
        exerciseRepository.save(exercise_2);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        mockMvc.perform(get("/api/chats/{id}/exercises?page=0&size=5", savedChat.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
    
    @Test
    void shouldCreateMeeting() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("Jane Doe");
        teacher.setEmail("jane.doe@outlook.com");
        teacher.setPassword("Bio.1234");
        Teacher savedTeacher = teacherRepository.save(teacher);

        Chat chat = new Chat();
        chat.setSubject("Biologia");
        chat.setTeacher(teacher);
        Chat savedChat = chatRepository.save(chat);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedTeacher);

        MeetingRequestDTO dto = new MeetingRequestDTO();
        dto.setTitle("Aula de Biologia");
        dto.setDescription("Assistam");
        dto.setLink("example.drive.com");
        dto.setDate(LocalDateTime.MAX);

        mockMvc.perform(post("/api/chats/{idChat}/meetings", savedChat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(dto.getTitle()));
    }

    @Test
    void shouldListMeetings() throws Exception {
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

        Meeting meeting_1 = new Meeting();
        meeting_1.setTitle("Aula de Biologia");
        meeting_1.setDescription("Assistam");
        meeting_1.setLink("example.drive.com");
        meeting_1.setChat(savedChat);
        meeting_1.setDate(LocalDateTime.MAX);
        meetingRepository.save(meeting_1);

        Meeting meeting_2 = new Meeting();
        meeting_2.setTitle("Revisao de Biologia");
        meeting_2.setDescription("Relembrem");
        meeting_2.setLink("example.drive.com");
        meeting_2.setChat(savedChat);
        meeting_2.setDate(LocalDateTime.MAX);
        meetingRepository.save(meeting_2);

        JwtService jwtService = new JwtService(jwtEncoder);
        String token = jwtService.generateToken(savedStudent);

        mockMvc.perform(get("/api/chats/{id}/meetings?page=0&size=5", savedChat.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}
