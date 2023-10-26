package edu.school.admin.service;

import edu.school.admin.dto.request.DeRegisterDto;
import edu.school.admin.dto.request.RegisterDto;
import edu.school.admin.dto.response.StudentsDto;
import edu.school.admin.dto.response.TeachersDto;
import edu.school.admin.entity.Registration;

import java.util.List;

public interface RegistrationService {
    List<Registration> register(RegisterDto registerDto);

    Registration deRegister(DeRegisterDto deRegisterDto);

    StudentsDto retrieveCommonStudents(String[] teachers);

    TeachersDto retrieveAllTeachers();
}
