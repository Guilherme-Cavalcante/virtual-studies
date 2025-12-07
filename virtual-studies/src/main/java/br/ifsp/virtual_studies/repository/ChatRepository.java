package br.ifsp.virtual_studies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Teacher;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByTeacher(Teacher teacher, Pageable pageable);
}
