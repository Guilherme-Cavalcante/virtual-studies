package br.ifsp.virtual_studies.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.virtual_studies.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Page<Task> findAllTasks(int sort, Pageable pageable);
    // Page<Student> findBy(Pageable pageable);
    Optional<User> findByName(String name);
}
