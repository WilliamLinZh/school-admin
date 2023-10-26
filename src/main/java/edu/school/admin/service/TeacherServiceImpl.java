package edu.school.admin.service;

import edu.school.admin.entity.Teacher;
import edu.school.admin.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public boolean existByEmail(String teacherEmail) {
        return teacherRepository.existsByEmail(teacherEmail);
    }

}
