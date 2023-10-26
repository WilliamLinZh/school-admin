package edu.school.admin.service;

import edu.school.admin.entity.Student;
import edu.school.admin.entity.Teacher;
import edu.school.admin.repository.StudentRepository;
import edu.school.admin.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class TeacherServiceImplTests {
    @MockBean
    TeacherRepository teacherRepository;

    @Autowired
    TeacherService teacherService;

    @Test
    public void shouldAddTeacher() {
        given(teacherRepository.save(any(Teacher.class))).willAnswer((invocation) -> invocation.getArgument(0));

        final Teacher teacher = new Teacher().setEmail("studentjane@gmail.com").setName("Jane");

        Teacher savedTeacher = teacherService.addTeacher(teacher);

        assertThat(savedTeacher).hasFieldOrPropertyWithValue("email", teacher.getEmail());
        assertThat(savedTeacher).hasFieldOrPropertyWithValue("name", teacher.getName());
    }

    @Test
    public void shouldReturnTrueWhenTeacherFoundWithEmails() {
        final String email = "teacherjane@gmail.com";

        final boolean teacherExists = true;

        given(teacherRepository.existsByEmail(any(String.class))).willReturn(teacherExists);

        boolean teachersExist = teacherService.existByEmail(email);

        assertThat(teachersExist).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenNoTeacherFoundWithEmails() {
        final String email = "teacherjane@gmail.com";

        final boolean teacherExists = false;

        given(teacherRepository.existsByEmail(any(String.class))).willReturn(teacherExists);

        boolean teachersExist = teacherRepository.existsByEmail(email);

        assertThat(teachersExist).isFalse();
    }
}
