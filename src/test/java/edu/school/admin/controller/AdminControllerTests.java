package edu.school.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school.admin.dto.request.DeRegisterDto;
import edu.school.admin.dto.request.RegisterDto;
import edu.school.admin.dto.response.*;
import edu.school.admin.entity.Registration;
import edu.school.admin.entity.Student;
import edu.school.admin.entity.Teacher;
import edu.school.admin.service.RegistrationService;
import edu.school.admin.service.StudentService;
import edu.school.admin.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private StudentService studentService;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private RegistrationService registrationService;

	@Test
	void shouldAddStudent() throws Exception {
		final Student student = new Student().setEmail("studentjane@gmail.com").setName("Jane");

		ResponseEntity responseEntity = new ResponseEntity("Student added successfully.");

		given(studentService.addStudent(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(student)))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(responseEntity)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddStudentWithNoStudent() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing student information.");

		given(studentService.addStudent(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddStudentWithNoStudentEmail() throws Exception {
		final Student student = new Student().setName("Jane");

		ErrorMessage message = new ErrorMessage("Missing student email.");

		given(studentService.addStudent(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(student)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddStudentWithNoStudentName() throws Exception {
		final Student student = new Student().setEmail("studentjane@gmail.com");

		ErrorMessage message = new ErrorMessage("Missing student name.");

		given(studentService.addStudent(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(student)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddStudentWithDuplicateStudentEmail() throws Exception {
		final Student student = new Student().setEmail("studentjane@gmail.com").setName("Jane");

		ErrorMessage message = new ErrorMessage("Student email already exists!");

		given(studentService.addStudent(any(Student.class))).willThrow(new DataIntegrityViolationException(""));

		this.mockMvc.perform(post("/api/students")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(student)))
				.andExpect(status().isConflict())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldAddTeacher() throws Exception {
		final Teacher teacher = new Teacher().setEmail("teachermary@gmail.com").setName("Mary");

		ResponseEntity responseEntity = new ResponseEntity("Teacher added successfully.");

		given(teacherService.addTeacher(any(Teacher.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(teacher)))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(responseEntity)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddTeacherWithNoTeacher() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing teacher information.");

		given(teacherService.addTeacher(any(Teacher.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddTeacherWithNoTeacherEmail() throws Exception {
		final Teacher teacher = new Teacher().setName("Jane");

		ErrorMessage message = new ErrorMessage("Missing teacher email.");

		given(teacherService.addTeacher(any(Teacher.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(teacher)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddTeacherWithNoTeacherName() throws Exception {
		final Teacher teacher = new Teacher().setEmail("teacherjane@gmail.com");

		ErrorMessage message = new ErrorMessage("Missing teacher name.");

		given(teacherService.addTeacher(any(Teacher.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(teacher)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddTeacherWithDuplicateTeacherEmail() throws Exception {
		final Teacher teacher = new Teacher().setEmail("teacherjane@gmail.com").setName("Jane");

		ErrorMessage message = new ErrorMessage("Teacher email already exists!");

		given(teacherService.addTeacher(any(Teacher.class))).willThrow(new DataIntegrityViolationException(""));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(teacher)))
				.andExpect(status().isConflict())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenAddTeacherWithOtherException() throws Exception {
		final Teacher teacher = new Teacher().setEmail("teacherjane@gmail.com").setName("Jane");

		ErrorMessage message = new ErrorMessage("Service encounter internal error.");

		given(teacherService.addTeacher(any(Teacher.class))).willThrow(new InvalidInvocationException(""));

		this.mockMvc.perform(post("/api/teachers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(teacher)))
				.andExpect(status().isInternalServerError())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldRegisterStudent() throws Exception {
		final RegisterDto registerDto = new RegisterDto().setTeacher("teacherken@gmail.com")
				.setStudents(Arrays.asList("studentjon@gmail.com","studenthon@gmail.com"));

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		ResponseEntity responseEntity = new ResponseEntity("Registration done.");

		given(teacherService.existByEmail(any(String.class))).willReturn(true);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(true);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(registerDto)))
				.andExpect(status().isNoContent())
				.andExpect(content().json(objectMapper.writeValueAsString(responseEntity)));
	}


	@Test
	void shouldReturnErrorMessageWhenRegisterStudentWithRegistrationInformation() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing registration information.");

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		given(teacherService.existByEmail(any(String.class))).willReturn(true);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(true);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenRegisterStudentWithNoTeacherEmail() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing teacher email.");

		final RegisterDto registerDto = new RegisterDto()
				.setStudents(Arrays.asList("studentjon@gmail.com","studenthon@gmail.com"));

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		ResponseEntity responseEntity = new ResponseEntity("Registration done.");

		given(teacherService.existByEmail(any(String.class))).willReturn(true);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(true);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(registerDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenRegisterStudentWithNoStudentEmail() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing  students emails.");

		final RegisterDto registerDto = new RegisterDto().setTeacher("teacherken@gmail.com");

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		ResponseEntity responseEntity = new ResponseEntity("Registration done.");

		given(teacherService.existByEmail(any(String.class))).willReturn(true);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(true);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(registerDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenRegisterStudentWithTeacherEmailAccountNotFound() throws Exception {
		ErrorMessage message = new ErrorMessage("Teacher email account not found.");

		final RegisterDto registerDto = new RegisterDto().setTeacher("teacherken@gmail.com")
				.setStudents(Arrays.asList("studentjon@gmail.com","studenthon@gmail.com"));

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		given(teacherService.existByEmail(any(String.class))).willReturn(false);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(true);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(registerDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenRegisterStudentWithStudentEmailAccountNotFound() throws Exception {
		ErrorMessage message = new ErrorMessage("Student email accounts not found or duplicate.");

		final RegisterDto registerDto = new RegisterDto().setTeacher("teacherken@gmail.com")
				.setStudents(Arrays.asList("studentjon@gmail.com","studenthon@gmail.com"));

		Registration registration1 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studentjon@gmail.com");
		Registration registration2 = new Registration().setRegistered(true).setTeacherEmail("teacherken@gmail.com").setStudentEmail("studenthon@gmail.com");
		final List<Registration> registrationList = Arrays.asList(registration1,registration2);

		given(teacherService.existByEmail(any(String.class))).willReturn(true);
		given(studentService.existByStudentEmails(any(List.class))).willReturn(false);
		given(registrationService.register(any(RegisterDto.class))).willAnswer((invocation) -> registrationList);

		this.mockMvc.perform(post("/api/register")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(registerDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldDeRegisterStudent() throws Exception {
		final DeRegisterDto deRegisterDto = new DeRegisterDto().setTeacher("teacherken@gmail.com")
				.setStudent("studentjon@gmail.com").setReason("Cancelled enrollment");
		final Registration registration = new Registration().setRegistered(false).setTeacherEmail("teacherken@gmail.com")
				.setStudentEmail("studentjon@gmail.com").setCancelReason("Cancelled enrollment");

		ResponseEntity responseEntity = new ResponseEntity("De-registration done.");

		given(registrationService.deRegister(any(DeRegisterDto.class))).willReturn(registration);

		this.mockMvc.perform(post("/api/deregister")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(deRegisterDto)))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(responseEntity)));
	}

	@Test
	void shouldReturnErrorMessageWhenDeRegisterStudentWithDeRegistrationInformation() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing deregister information.");

		final Registration registration = new Registration().setRegistered(false).setTeacherEmail("teacherken@gmail.com")
				.setStudentEmail("studentjon@gmail.com").setCancelReason("Cancelled enrollment");

		given(registrationService.deRegister(any(DeRegisterDto.class))).willReturn(registration);

		this.mockMvc.perform(post("/api/deregister")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenDeRegisterStudentWithNoTeacherEmail() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing teacher email.");

		final DeRegisterDto deRegisterDto = new DeRegisterDto()
				.setStudent("studentjon@gmail.com").setReason("Cancelled enrollment");
		final Registration registration = new Registration().setRegistered(false).setTeacherEmail("teacherken@gmail.com")
				.setStudentEmail("studentjon@gmail.com").setCancelReason("Cancelled enrollment");

		given(registrationService.deRegister(any(DeRegisterDto.class))).willReturn(registration);

		this.mockMvc.perform(post("/api/deregister")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(deRegisterDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenDeRegisterStudentWithNoStudentEmail() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing student email.");

		final DeRegisterDto deRegisterDto = new DeRegisterDto().setTeacher("teacherken@gmail.com")
				.setReason("Cancelled enrollment");
		final Registration registration = new Registration().setRegistered(false).setTeacherEmail("teacherken@gmail.com")
				.setStudentEmail("studentjon@gmail.com").setCancelReason("Cancelled enrollment");

		given(registrationService.deRegister(any(DeRegisterDto.class))).willReturn(registration);

		this.mockMvc.perform(post("/api/deregister")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(deRegisterDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldReturnErrorMessageWhenDeRegisterStudentWithNoCancelReason() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing cancel reason.");

		final DeRegisterDto deRegisterDto = new DeRegisterDto().setTeacher("teacherken@gmail.com")
				.setStudent("studentjon@gmail.com");
		final Registration registration = new Registration().setRegistered(false).setTeacherEmail("teacherken@gmail.com")
				.setStudentEmail("studentjon@gmail.com").setCancelReason("Cancelled enrollment");

		given(registrationService.deRegister(any(DeRegisterDto.class))).willReturn(registration);

		this.mockMvc.perform(post("/api/deregister")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(deRegisterDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldRetrieveCommonStudents() throws Exception {
		StudentsDto studentsDto = new StudentsDto().setStudents(Arrays.asList("commonstudent1@gmail.com", "commonstudent2@gmail.com", "student_only_under_teacher_ken@gmail.com"));

		given(registrationService.retrieveCommonStudents(any(String[].class))).willReturn(studentsDto);

		this.mockMvc.perform(get("/api/commonstudents?teacher=teacherken%40gmail.com&teacher=teacherjoe%40gmail.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.students.size()", is(studentsDto.getStudents().size())));
	}

	@Test
	void shouldReturnErrorMessageWhenRetrieveCommonStudentsWithNoTeachers() throws Exception {
		ErrorMessage message = new ErrorMessage("Missing teacher emails.");

		StudentsDto studentsDto = new StudentsDto().setStudents(Arrays.asList("commonstudent1@gmail.com", "commonstudent2@gmail.com", "student_only_under_teacher_ken@gmail.com"));

		given(registrationService.retrieveCommonStudents(any(String[].class))).willReturn(studentsDto);

		this.mockMvc.perform(get("/api/commonstudents"))
				.andExpect(status().isBadRequest())
				.andExpect(content().json(objectMapper.writeValueAsString(message)));
	}

	@Test
	void shouldRetrieveAllTeachers() throws Exception {
		TeacherDto teacherDto1 = new TeacherDto().setEmail("teachermary@gmail.com")
				.setStudents(Arrays.asList("studentjane@gmail.com","studentjon@gmail.com"));
		TeacherDto teacherDto2 = new TeacherDto().setEmail("teacherken@gmail.com")
				.setStudents(Arrays.asList("studentjane@gmail.com","studentjon@gmail.com"));
		TeachersDto teachersDto = new TeachersDto().setTeachers(Arrays.asList(teacherDto1, teacherDto2));
		given(registrationService.retrieveAllTeachers()).willReturn(teachersDto);

		this.mockMvc.perform(get("/api/teachers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.teachers.size()", is(teachersDto.getTeachers().size())));
	}

}
