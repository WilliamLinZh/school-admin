package edu.school.admin.service;

import edu.school.admin.entity.Student;
import edu.school.admin.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    StudentRepository studentRepository;

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public boolean existByStudentEmails(List<String> studentEmails) {
        if ((new HashSet<>(studentEmails)).size() != studentEmails.size()) return false;

        List<Student> students = studentRepository.getByEmailIn(studentEmails);
        return studentEmails.size() == students.size();
    }
}
