package br.ifsp.virtual_studies.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.meeting.MeetingPatchDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Meeting;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MeetingRepository;
import br.ifsp.virtual_studies.repository.MeetingRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MeetingControllerTest {

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
    void shouldGetMeetingById() throws Exception {
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

        Meeting meeting = new Meeting();
        meeting.setTitle("Aula de Biologia");
        meeting.setDescription("Assistam");
        meeting.setLink("example.drive.com");
        meeting.setChat(savedChat);
        meetingRepository.save(meeting);

        mockMvc.perform(get("/api/meetings/{idMeeting}", meeting.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(meeting.getTitle()));
    }

    @Test
    void shouldEditMeeting() throws Exception {
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

        Meeting meeting = new Meeting();
        meeting.setTitle("Aula de Biologia");
        meeting.setDescription("Assistam");
        meeting.setLink("example.drive.com");
        meeting.setDate(LocalDateTime.MAX);
        meeting.setChat(savedChat);
        meetingRepository.save(meeting);

        MeetingPatchDTO dto = new MeetingPatchDTO();
        dto.setDescription(Optional.of("Facam atentamente"));

        mockMvc.perform(put("/api/meetings/{idMeeting}", meeting.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(dto.getDescription().get()));
    }

    @Test
    void shouldDeleteMeeting() throws Exception {
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

        Meeting meeting = new Meeting();
        meeting.setTitle("Aula de Biologia");
        meeting.setDescription("Assistam");
        meeting.setLink("example.drive.com");
        meeting.setDate(LocalDateTime.MAX);
        meeting.setChat(savedChat);
        meetingRepository.save(meeting);

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

        Meeting savedMeeting = meetingRepository.findByChat(savedChat, pageable)
                .toList()
                .getFirst();

        mockMvc.perform(delete("/api/meetings/{idMeeting}", savedMeeting.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

}
