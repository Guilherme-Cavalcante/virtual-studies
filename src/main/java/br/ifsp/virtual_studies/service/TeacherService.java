package br.ifsp.virtual_studies.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.dto.teacher.TeacherRequestDTO;
import br.ifsp.virtual_studies.dto.teacher.TeacherResponseDTO;
import br.ifsp.virtual_studies.exceptions.ResourceNotFoundException;
import br.ifsp.virtual_studies.mapper.PagedResponseMapper;
import br.ifsp.virtual_studies.model.Teacher;
import br.ifsp.virtual_studies.repository.TeacherRepository;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final PagedResponseMapper pagedResponseMapper;
    
    public TeacherService(TeacherRepository teacherRepository, ModelMapper modelMapper, PagedResponseMapper pagedResponseMapper) {
        this.teacherRepository = teacherRepository;
        this.modelMapper = modelMapper;
        this.pagedResponseMapper = pagedResponseMapper;
    }
    
    public TeacherResponseDTO createTeacher(TeacherRequestDTO teacherDto) {
        Teacher teacher = modelMapper.map(teacherDto, Teacher.class);
        Teacher createdTeacher = teacherRepository.save(teacher);
        return modelMapper.map(createdTeacher, TeacherResponseDTO.class);
    }
    
    public PagedResponse<TeacherResponseDTO> getAllTeachers(Pageable pageable) {
        Page<Teacher> teachersPage = teacherRepository.findAll(pageable);
        return pagedResponseMapper.toPagedResponse(teachersPage, TeacherResponseDTO.class);
    }
    
    public TeacherResponseDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return modelMapper.map(teacher, TeacherResponseDTO.class);
    }
    
    public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO teacherDto) {
        
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
        
        modelMapper.map(teacherDto, existingTeacher);
        existingTeacher.setId(id);
        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return modelMapper.map(updatedTeacher, TeacherResponseDTO.class);
    }
    
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + id));
        
        teacherRepository.delete(teacher);
    }
}