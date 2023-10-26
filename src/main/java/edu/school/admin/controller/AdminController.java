package edu.school.admin.controller;

import edu.school.admin.dto.request.DeRegisterDto;
import edu.school.admin.dto.request.RegisterDto;
import edu.school.admin.dto.response.ResponseEntity;
import edu.school.admin.dto.response.StudentsDto;
import edu.school.admin.dto.response.TeachersDto;
import edu.school.admin.entity.Student;
import edu.school.admin.entity.Teacher;
import edu.school.admin.exception.DuplicateDataException;
import edu.school.admin.exception.InvalidException;
import edu.school.admin.service.RegistrationService;
import edu.school.admin.service.StudentService;
import edu.school.admin.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    RegistrationService registrationService;

    @PostMapping("/students")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity addStudent(@RequestBody(required = false) Student student) {
        if (student == null) {
            throw new InvalidException("Missing student information.");
        } else if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new InvalidException("Missing student email.");
        } else if (student.getName() == null || student.getName().isEmpty()) {
            throw new InvalidException("Missing student name.");
        }

        try {
            studentService.addStudent(student);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateDataException("Student email already exists!");
        }
        return new ResponseEntity("Student added successfully.");
    }

    @PostMapping("/teachers")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity addTeacher(@RequestBody(required = false) Teacher teacher) {
        if (teacher == null) {
            throw new InvalidException("Missing teacher information.");
        } else if (teacher.getEmail() == null || teacher.getEmail().isEmpty()) {
            throw new InvalidException("Missing teacher email.");
        } else if (teacher.getName() == null || teacher.getName().isEmpty()) {
            throw new InvalidException("Missing teacher name.");
        }

        try {
            teacherService.addTeacher(teacher);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateDataException("Teacher email already exists!");
        }
        return new ResponseEntity("Teacher added successfully.");
    }

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    ResponseEntity registerStudent(@RequestBody(required = false) RegisterDto registerDto) {
        if (registerDto == null) {
            throw new InvalidException("Missing registration information.");
        } else if (registerDto.getTeacher() == null || registerDto.getTeacher().isEmpty()) {
            throw new InvalidException("Missing teacher email.");
        } else if (registerDto.getStudents() == null || registerDto.getStudents().size() == 0) {
            throw new InvalidException("Missing  students emails.");
        }
        if (!teacherService.existByEmail(registerDto.getTeacher())) {
            throw new InvalidException("Teacher email account not found.");
        }
        if (!studentService.existByStudentEmails(registerDto.getStudents())) {
            throw new InvalidException("Student email accounts not found or duplicate.");
        }
        registrationService.register(registerDto);
        return new ResponseEntity("Registration done.");
    }

    @PostMapping("/deregister")
    ResponseEntity deRegisterStudent(@RequestBody(required = false) DeRegisterDto deRegisterDto) {
        if (deRegisterDto == null) {
            throw new InvalidException("Missing deregister information.");
        } else if (deRegisterDto.getTeacher() == null || deRegisterDto.getTeacher().isEmpty()) {
            throw new InvalidException("Missing teacher email.");
        } else if (deRegisterDto.getStudent() == null || deRegisterDto.getStudent().isEmpty()) {
            throw new InvalidException("Missing student email.");
        } else if (deRegisterDto.getReason() == null || deRegisterDto.getReason().isEmpty()) {
            throw new InvalidException("Missing cancel reason.");
        }
        registrationService.deRegister(deRegisterDto);
        return new ResponseEntity("De-registration done.");
    }

    @GetMapping("/commonstudents")
    StudentsDto retrieveCommonStudents(@RequestParam(value = "teacher", required = false) String[] teachers) {
        if (teachers == null || teachers.length == 0) {
            throw new InvalidException("Missing teacher emails.");
        }
        return registrationService.retrieveCommonStudents(teachers);
    }

    @GetMapping("/teachers")
    TeachersDto retrieveAllTeachers() {
        return registrationService.retrieveAllTeachers();
    }
}
