package br.ifsp.virtual_studies.student;

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

import br.ifsp.virtual_studies.dto.student.StudentRequestDTO;
import br.ifsp.virtual_studies.dto.student.StudentResponseDTO;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.service.StudentService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PagedResponseMapper pagedResponseMapper;

    @InjectMocks
    private StudentService studentService;

    // @Test
    // void shouldCreateStudent() {
    //     StudentRequestDTO dto = new StudentRequestDTO();
    //     dto.setName("John Doe");
    //     dto.setEmail("john.doe@hotmail.com");
    //     dto.setPassword("John1234.");

    //     Student studentEntity = new Student();
    //     Student savedStudent = new Student();
    //     savedStudent.setId(1L);
    //     savedStudent.setName("John Doe");

    //     when(modelMapper.map(dto, Student.class)).thenReturn(studentEntity);
    //     when(studentRepository.save(any())).thenReturn(savedStudent);
    //     when(modelMapper.map(savedStudent, StudentResponseDTO.class)).thenReturn(new StudentResponseDTO());

    //     StudentResponseDTO response = studentService.createStudent(dto);
    //     assertNotNull(response);
    // }

    @Test
    void shouldFetchStudent() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        when(modelMapper.map(any(), eq(StudentResponseDTO.class))).thenReturn(new StudentResponseDTO());

        StudentResponseDTO response = studentService.getStudentById(id);
        assertNotNull(response);
    }
}
