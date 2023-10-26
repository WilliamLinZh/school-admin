package edu.school.admin.service;

import edu.school.admin.entity.Student;
import edu.school.admin.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class StudentServiceImplTests {

    @MockBean
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Test
    public void shouldAddStudent() {
        given(studentRepository.save(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

        final Student student = new Student().setEmail("studentjane@gmail.com").setName("Jane");

        Student savedStudent = studentService.addStudent(student);

        assertThat(savedStudent).hasFieldOrPropertyWithValue("email", student.getEmail());
        assertThat(savedStudent).hasFieldOrPropertyWithValue("name", student.getName());
    }

    @Test
    public void shouldReturnTrueWhenAllStudentsFoundWithEmails() {
        final List<String> emails = Arrays.asList("studentjane@gmail.com","studenthon@gmail.com");

        final Student student1 = new Student().setEmail("studentjane@gmail.com").setName("Jane");
        final Student student2 = new Student().setEmail("studenthon@gmail.com").setName("Hon");
        final List<Student> studentList = Arrays.asList(student1, student2);

        given(studentRepository.getByEmailIn(any(List.class))).willReturn(studentList);


        boolean allStudentsExist = studentService.existByStudentEmails(emails);

        assertThat(allStudentsExist).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenNotAllStudentsFoundWithEmails() {
        final List<String> emails = Arrays.asList("studentjane@gmail.com","studenthon@gmail.com","studentken@gmail.com");

        final Student student1 = new Student().setEmail("studentjane@gmail.com").setName("Jane");
        final Student student2 = new Student().setEmail("studenthon@gmail.com").setName("Hon");
        final List<Student> studentList = Arrays.asList(student1, student2);

        given(studentRepository.getByEmailIn(any(List.class))).willReturn(studentList);


        boolean allStudentsExist = studentService.existByStudentEmails(emails);

        assertThat(allStudentsExist).isFalse();
    }
}
