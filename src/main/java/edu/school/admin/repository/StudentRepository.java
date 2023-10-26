package edu.school.admin.repository;

import edu.school.admin.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> getByEmailIn(List<String> studentEmails);
}
