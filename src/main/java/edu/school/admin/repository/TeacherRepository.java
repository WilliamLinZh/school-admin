package edu.school.admin.repository;

import edu.school.admin.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsByEmail(String teacherEmail);
}
