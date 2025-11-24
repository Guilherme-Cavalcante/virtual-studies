package br.ifsp.virtual_studies.usuario;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.virtual_studies.dto.usuario.UsuarioRegistrationDTO;
import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.repository.UsuarioRepository;
import jakarta.servlet.ServletContext;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void cleanDb() {
        usuarioRepository.deleteAll();
    }

    @Test
    void shouldCreateUsuario() throws Exception {
        UsuarioRegistrationDTO dto = new UsuarioRegistrationDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@hotmail.com");
        dto.setPassword("John1234.");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.password").value(dto.getPassword()));
    }

    @Test
    void shouldNotCreateUsuario() throws Exception {
        UsuarioRegistrationDTO dto = new UsuarioRegistrationDTO();
        dto.setName("");
        dto.setEmail("john.doe@");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // @Test
    // void shouldReturnUsuario() throws Exception {
    //     Usuario usuario = new Usuario();
    //     usuario.setName("John Doe");
    //     usuario.setEmail("john.doe@hotmail.com");
    //     usuario.setPassword("John1234.");
    //     Usuario saved = usuarioRepository.save(usuario);

    //     mockMvc.perform(get("/api/usuarios/" + saved.getId()))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.name").value(saved.getName()))
    //             .andExpect(jsonPath("$.email").value(saved.getEmail()))
    //             .andExpect(jsonPath("$.password").value(saved.getPassword()));
    // }
}
