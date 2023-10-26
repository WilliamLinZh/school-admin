package edu.school.admin.repository;

import edu.school.admin.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    List<Registration> getByRegisteredAndTeacherEmailAndStudentEmail(boolean b, String teacher, String student);

    List<Registration> getByRegisteredAndTeacherEmailIn(boolean b, List<String> asList);

    List<Registration> getByRegistered(boolean b);

}
