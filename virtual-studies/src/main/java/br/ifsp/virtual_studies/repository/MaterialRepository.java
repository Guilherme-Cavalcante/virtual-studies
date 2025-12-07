package br.ifsp.virtual_studies.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.virtual_studies.model.Chat;
import br.ifsp.virtual_studies.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    //Page<Task> findAllTasks(int sort, Pageable pageable);
    //Page<Student> findBy(Pageable pageable);
    Page<Material> findByChat(Chat chat, Pageable pageable);
}
