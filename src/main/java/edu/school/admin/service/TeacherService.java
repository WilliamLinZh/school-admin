package edu.school.admin.service;

import edu.school.admin.entity.Teacher;

public interface TeacherService {
    Teacher addTeacher(Teacher teacher);

    boolean existByEmail(String teacherEmail);
}
