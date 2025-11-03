package br.ifsp.virtual_studies.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.virtual_studies.dto.teacher.TeacherResponseDTO;
import br.ifsp.virtual_studies.dto.teacher.TeacherRequestDTO;
import br.ifsp.virtual_studies.dto.page.PagedResponse;
import br.ifsp.virtual_studies.service.TeacherService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    
    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO teacher) {
        TeacherResponseDTO teacherResponseDTO = teacherService.createTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherResponseDTO);
    }
    
    @GetMapping
    public ResponseEntity<PagedResponse<TeacherResponseDTO>> getAllTeachers(Pageable pageable) {
        return ResponseEntity.ok(teacherService.getAllTeachers(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id,
            @Valid @RequestBody TeacherRequestDTO teacherDto) {
        TeacherResponseDTO updatedTeacher = teacherService.updateTeacher(id, teacherDto);
        return ResponseEntity.ok(updatedTeacher);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
