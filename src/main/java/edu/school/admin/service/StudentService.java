package edu.school.admin.service;

import edu.school.admin.entity.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);

    boolean existByStudentEmails(List<String> studentEmails);
}
