package edu.school.admin.service;

import edu.school.admin.dto.request.DeRegisterDto;
import edu.school.admin.dto.request.RegisterDto;
import edu.school.admin.dto.response.StudentsDto;
import edu.school.admin.dto.response.TeachersDto;
import edu.school.admin.entity.Registration;
import edu.school.admin.exception.InvalidException;
import edu.school.admin.repository.RegistrationRepository;
import edu.school.admin.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class RegistrationServiceImplTests {

    @MockBean
    RegistrationRepository registrationRepository;

    @MockBean
    TeacherRepository teacherRepository;

    @Autowired
    RegistrationService registrationService;

    @Test
    public void shouldRegister() {
        given(registrationRepository.saveAllAndFlush(any(List.class))).willAnswer((invocation) -> invocation.getArgument(0));

        final RegisterDto registerDto = new RegisterDto().setTeacher("teacherken@gmail.com")
                .setStudents(Arrays.asList("studentjon@gmail.com", "studenthon@gmail.com"));

        List<Registration> registrations = registrationService.register(registerDto);

        assertThat(registrations.size()).isEqualTo(registerDto.getStudents().size());
    }

    @Test
    public void shouldDeRegister() {
        final Registration registration1 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com").setRegistered(false).setCancelReason("Cancelled enrollment").setId(0);
        final Registration registration2 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com").setRegistered(false).setCancelReason("Cancelled enrollment").setId(1);
        final List<Registration> registrationList = Arrays.asList(registration1,registration2);

        given(registrationRepository.getByRegisteredAndTeacherEmailAndStudentEmail(any(Boolean.class), any(String.class), any(String.class))).willReturn(registrationList);
        given(registrationRepository.save(any(Registration.class))).willAnswer((invocation) -> invocation.getArgument(0));

        final DeRegisterDto deRegisterDto = new DeRegisterDto().setTeacher("teacherken@gmail.com")
                .setStudent("studentjon@gmail.com")
                .setReason("Cancelled enrollment");

        Registration registration = registrationService.deRegister(deRegisterDto);

        assertThat(registration).hasFieldOrPropertyWithValue("teacherEmail", registration1.getTeacherEmail());
        assertThat(registration).hasFieldOrPropertyWithValue("studentEmail", registration1.getStudentEmail());
        assertThat(registration).hasFieldOrPropertyWithValue("cancelReason", registration1.getCancelReason());
        assertThat(registration).hasFieldOrPropertyWithValue("registered", false);
        assertThat(registration).hasFieldOrPropertyWithValue("id", 0);
    }

    @Test
    public void shouldThrowInvalidExceptionWhenDeRegisterWithNotExistRegistation() {
        given(registrationRepository.save(any(Registration.class))).willAnswer((invocation) -> invocation.getArgument(0));

        final DeRegisterDto deRegisterDto = new DeRegisterDto().setTeacher("teacherken@gmail.com")
                .setStudent("studentjon@gmail.com")
                .setReason("Cancelled enrollment");


        assertThrows(InvalidException.class,
                () -> registrationService.deRegister(deRegisterDto));
    }

    @Test
    public void shouldRetrieveCommonStudents() {
        final Registration registration1 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration2 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final Registration registration3 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration4 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final List<Registration> registrationList = Arrays.asList(registration1,registration2,registration3,registration4);

        given(registrationRepository.getByRegisteredAndTeacherEmailIn(any(Boolean.class),any(List.class))).willReturn(registrationList);

        String[] teachers = new String[] {"teacherken@gmail.com","teacherjoe@gmail.com"};

        StudentsDto studentsDto = registrationService.retrieveCommonStudents(teachers);

        assertThat(studentsDto.getStudents().size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnEmptyStudentListWhenRetrieveCommonStudentsWithTeacherNotHasStudents() {
        final Registration registration1 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration2 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final Registration registration3 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration4 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final List<Registration> registrationList = Arrays.asList(registration1,registration2,registration3,registration4);

        given(registrationRepository.getByRegisteredAndTeacherEmailIn(any(Boolean.class),any(List.class))).willReturn(registrationList);

        String[] teachers = new String[] {"teacherken@gmail.com","teacherjoe@gmail.com","teacherjane@gmail.com"};

        StudentsDto studentsDto = registrationService.retrieveCommonStudents(teachers);

        assertThat(studentsDto.getStudents().size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnEmptyStudentListWhenRetrieveCommonStudentsWithTeachersNotHaveCommonStudents() {
        final Registration registration1 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration2 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final Registration registration3 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent3@gmail.com").setRegistered(true);
        final Registration registration4 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent4@gmail.com").setRegistered(true);
        final List<Registration> registrationList = Arrays.asList(registration1,registration2,registration3,registration4);

        given(registrationRepository.getByRegisteredAndTeacherEmailIn(any(Boolean.class),any(List.class))).willReturn(registrationList);

        String[] teachers = new String[] {"teacherken@gmail.com","teacherjoe@gmail.com"};

        StudentsDto studentsDto = registrationService.retrieveCommonStudents(teachers);

        assertThat(studentsDto.getStudents().size()).isEqualTo(0);
    }

    @Test
    public void shouldRetrieveAllTeachers() {
        final Registration registration1 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration2 = new Registration().setTeacherEmail("teacherken@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final Registration registration3 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent1@gmail.com").setRegistered(true);
        final Registration registration4 = new Registration().setTeacherEmail("teacherjoe@gmail.com").setStudentEmail("commonstudent2@gmail.com").setRegistered(true);
        final List<Registration> registrationList = Arrays.asList(registration1,registration2,registration3,registration4);

        given(registrationRepository.getByRegistered(any(Boolean.class))).willReturn(registrationList);

        given(teacherRepository.findAll()).willReturn(new ArrayList<>());


        TeachersDto teachersDto = registrationService.retrieveAllTeachers();

        assertThat(teachersDto.getTeachers().size()).isEqualTo(2);
        assertThat(teachersDto.getTeachers().get(0).getStudents().size()).isEqualTo(2);
        assertThat(teachersDto.getTeachers().get(1).getStudents().size()).isEqualTo(2);
    }

}
