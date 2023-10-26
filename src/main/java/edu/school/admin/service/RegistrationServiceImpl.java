package edu.school.admin.service;

import edu.school.admin.dto.request.DeRegisterDto;
import edu.school.admin.dto.request.RegisterDto;
import edu.school.admin.dto.response.StudentsDto;
import edu.school.admin.dto.response.TeacherDto;
import edu.school.admin.dto.response.TeachersDto;
import edu.school.admin.entity.Registration;
import edu.school.admin.exception.InvalidException;
import edu.school.admin.repository.RegistrationRepository;
import edu.school.admin.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements RegistrationService{
    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public List<Registration> register(RegisterDto registerDto) {
        List<Registration> registrations = registerDto.getStudents().stream()
                .map(studentEmail -> new Registration().setTeacherEmail(registerDto.getTeacher()).setStudentEmail(studentEmail).setRegistered(true))
                .collect(Collectors.toList());
        return registrationRepository.saveAllAndFlush(registrations);
    }

    @Override
    public Registration deRegister(DeRegisterDto deRegisterDto) {
        List<Registration> retrievedRegistrationList = registrationRepository.getByRegisteredAndTeacherEmailAndStudentEmail(true, deRegisterDto.getTeacher(), deRegisterDto.getStudent());
        if (retrievedRegistrationList == null || retrievedRegistrationList.size() == 0) {
            throw new InvalidException("Matching registration not found.");
        }

        Registration updatedRegistration = retrievedRegistrationList.get(0).setRegistered(false).setCancelReason(deRegisterDto.getReason());
        return registrationRepository.save(updatedRegistration);
    }

    @Override
    public StudentsDto retrieveCommonStudents(String[] teachers) {
        List<Registration> registration = registrationRepository.getByRegisteredAndTeacherEmailIn(true, Arrays.asList(teachers));

        BinaryOperator<List<String>> stringListMergeFunction = (a, b) ->{
            List<String> result = new ArrayList<>();
            result.addAll(a);
            result.addAll(b);
            return result;
        };

        List<List<String>> studentsCollection = registration.stream()
                .collect(Collectors.toMap(Registration::getTeacherEmail, registration1 -> Arrays.asList(registration1.getStudentEmail()), stringListMergeFunction))
                .values()
                .stream()
                .sorted(Comparator.comparingInt(List::size))
                .toList();

        if (studentsCollection.size() != (new HashSet<>(Arrays.asList(teachers))).size()) {
            return new StudentsDto(new ArrayList<>());
        } else {
            List<String> commonStudents = studentsCollection.get(0);
            for (List<String> studentEmails : studentsCollection) {
                commonStudents.retainAll(studentEmails);
                if (commonStudents.size() == 0) {
                    return new StudentsDto(new ArrayList<>());
                }
            }
            return new StudentsDto().setStudents(commonStudents);
        }
    }

    @Override
    public TeachersDto retrieveAllTeachers() {
        List<TeacherDto> teacherDtoList = new ArrayList<>();

        // All teachers without student registered also shown
        List<Registration> registrationsForAllTeacher = teacherRepository.findAll().stream()
                .map(teacher -> new Registration().setTeacherEmail(teacher.getEmail()))
                .collect(Collectors.toList());

        List<Registration> registrations = registrationRepository.getByRegistered(true);
        registrations.addAll(registrationsForAllTeacher);
        registrations.stream()
                .collect(Collectors.groupingBy(Registration::getTeacherEmail))
                .values()
                .forEach(registrationList -> {
                   String teacherEmail = registrationList.get(0).getTeacherEmail();

                   List<String> students = new ArrayList<>();
                   registrationList.forEach(registration -> {if (registration.getStudentEmail() != null) students.add(registration.getStudentEmail());});

                    teacherDtoList.add(new TeacherDto().setEmail(teacherEmail).setStudents(students));
                });
        return new TeachersDto(teacherDtoList);
    }
}
