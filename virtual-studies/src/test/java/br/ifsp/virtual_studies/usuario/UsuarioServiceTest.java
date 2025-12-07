package br.ifsp.virtual_studies.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.ifsp.virtual_studies.dto.usuario.UsuarioRegistrationDTO;
import br.ifsp.virtual_studies.dto.usuario.UsuarioResponseDTO;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Usuario;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.UsuarioRepository;
import br.ifsp.virtual_studies.service.UsuarioService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PagedResponseMapper pagedResponseMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void shouldCreateStudent() {
        UsuarioRegistrationDTO dto = new UsuarioRegistrationDTO();
        dto.setName("John Doe");
        dto.setEmail("john.doe@hotmail.com");
        dto.setPassword("John1234.");

        Student usuarioEntity = new Student();
        Student savedUsuario = new Student();
        savedUsuario.setId(1L);
        savedUsuario.setName("John Doe");

        when(modelMapper.map(dto, Student.class)).thenReturn(usuarioEntity);
        when(studentRepository.save(any())).thenReturn(savedUsuario);
        when(modelMapper.map(savedUsuario, UsuarioResponseDTO.class)).thenReturn(new UsuarioResponseDTO());

        UsuarioResponseDTO response = usuarioService.createUsuario(dto);
        assertNotNull(response);
    }

    // @Test
    // void shouldFetchUsuario() {
    //     Long id = 1L;
    //     Student usuario = new Student();
    //     usuario.setId(id);

    //     when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
    //     when(modelMapper.map(any(), eq(UsuarioResponseDTO.class))).thenReturn(new UsuarioResponseDTO());

    //     UsuarioResponseDTO response = usuarioService.getUsuarioById(id);
    //     assertNotNull(response);
    // }
}
