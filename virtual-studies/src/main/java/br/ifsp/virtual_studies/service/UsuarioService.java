package br.ifsp.virtual_studies.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.student.StudentResponseDTO;
import br.ifsp.virtual_studies.dto.teacher.TeacherResponseDTO;
import br.ifsp.virtual_studies.dto.usuario.UsuarioRegistrationDTO;
import br.ifsp.virtual_studies.dto.usuario.UsuarioResponseDTO;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Student;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.StudentRepository;
import br.ifsp.virtual_studies.repository.TeacherRepository;

@Service
public class UsuarioService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;

    public UsuarioService(StudentRepository studentRepository, TeacherRepository teacherRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }

    public UsuarioResponseDTO createUsuario(UsuarioRegistrationDTO usuarioDto) {
        if (usuarioDto.isTeacher()) {
            Teacher teacher = modelMapper.map(usuarioDto, Teacher.class);
            Teacher createdTeacher = teacherRepository.save(teacher);
            return modelMapper.map(createdTeacher, UsuarioResponseDTO.class);
        } else {
            Student student = modelMapper.map(usuarioDto, Student.class);
            Student createdStudent = studentRepository.save(student);
            return modelMapper.map(createdStudent, UsuarioResponseDTO.class);
        }
    }
}
