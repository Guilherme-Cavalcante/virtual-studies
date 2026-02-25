package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.student.StudentRequestDTO;
import br.ifsp.virtual_studies.dto.student.StudentResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public StudentResponseDTO createStudent(StudentRequestDTO studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        student.setCreatedAt(LocalDateTime.now());
        Student createdStudent = studentRepository.save(student);
        return modelMapper.map(createdStudent, StudentResponseDTO.class);
    }
    
    public PagedResponse<StudentResponseDTO> getAllStudents(Pageable pageable) {
        Page<Student> studentsPage = studentRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(studentsPage, StudentResponseDTO.class);
    }
    
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return modelMapper.map(student, StudentResponseDTO.class);
    }
    
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO studentDto) {
        
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        modelMapper.map(studentDto, existingStudent);
        existingStudent.setId(id);
        Student updatedStudent = studentRepository.save(existingStudent);
        return modelMapper.map(updatedStudent, StudentResponseDTO.class);
    }
    
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        studentRepository.delete(student);
    }
}