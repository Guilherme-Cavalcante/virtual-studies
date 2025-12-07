package br.ifsp.virtual_studies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findAllByChatsContaining(Chat chat, Pageable pageable);
}
