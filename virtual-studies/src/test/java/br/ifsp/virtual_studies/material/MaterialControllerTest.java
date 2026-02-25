package br.ifsp.virtual_studies.material;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
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

import br.ifsp.virtual_studies.dto.chat.ChatResponseDTO;
import br.ifsp.virtual_studies.dto.material.MaterialPatchDTO;
import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.model.Material;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.ChatRepository;
import br.ifsp.virtual_studies.repository.MaterialRepository;
import br.ifsp.virtual_studies.repository.MaterialRepository;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;
import br.ifsp.virtual_studies.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MaterialControllerTest {

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
    private MaterialRepository materialRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void cleanDb() {
        chatRepository.deleteAll();
        teacherRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldGetMaterialById() throws Exception {
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

        Material material = new Material();
        material.setTitle("Aula de Biologia");
        material.setDescription("Assistam");
        material.setLocal("example.drive.com");
        material.setChat(savedChat);
        materialRepository.save(material);

        mockMvc.perform(get("/api/materials/{idMaterial}", material.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(material.getTitle()));
    }

    @Test
    void shouldEditMaterial() throws Exception {
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

        Material material = new Material();
        material.setTitle("Aula de Biologia");
        material.setDescription("Assistam");
        material.setLocal("example.drive.com");
        material.setChat(savedChat);
        materialRepository.save(material);

        MaterialPatchDTO dto = new MaterialPatchDTO();
        dto.setDescription(Optional.of("Assistam atentamente"));

        mockMvc.perform(put("/api/materials/{idMaterial}", material.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(dto.getDescription().get()));
    }

    @Test
    void shouldDeleteMaterial() throws Exception {
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

        Material material = new Material();
        material.setTitle("Aula de Biologia");
        material.setDescription("Assistam");
        material.setLocal("example.drive.com");
        material.setChat(savedChat);
        materialRepository.save(material);

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

        Material savedMaterial = materialRepository.findByChat(savedChat, pageable)
                .toList()
                .getFirst();

        mockMvc.perform(delete("/api/materials/{idMaterial}", savedMaterial.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

}
