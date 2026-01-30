package com.maestrocoach.service;

import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.persistence.InMemoryStudentStore;
import com.maestrocoach.persistence.InMemoryTeacherStore;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    @Test
    void createStudent_success() {
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryTeacherStore teacherStore = new InMemoryTeacherStore();
        StudentService studentService = new StudentService(studentStore, teacherStore);

        Student student = studentService.createStudent("Anna Bellman", "anna@bellman.com", "piano");

        assertNotNull(student);
        assertEquals("Anna Bellman", student.getFullName());
        assertEquals("anna@bellman.com", student.getEmail());
        assertEquals("piano", student.getInstrument());
        assertEquals(1, studentStore.findAll().size());
    }

    @Test
    void assignStudentToTeacher_success() {
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryTeacherStore teacherStore = new InMemoryTeacherStore();
        StudentService studentService = new StudentService(studentStore, teacherStore);

        Teacher teacher = teacherStore.save(new Teacher("John Doe", "john@school.com"));
        Student student = studentService.createStudent("Anna Bellman", "anna@bellman.com", "piano");

        assertNull(student.getTeacherId());

        studentService.assignStudentToTeacher(student.getId(), teacher.getId());

        Student studentWithTeacher = studentStore.findById(student.getId()).orElseThrow();

        assertEquals(teacher.getId(), studentWithTeacher.getTeacherId());
    }

    @Test
    void assignStudentToTeacher_studentNotFound_throwsIllegalArgumentException() {
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryTeacherStore teacherStore = new InMemoryTeacherStore();
        StudentService studentService = new StudentService(studentStore, teacherStore);

        Teacher teacher = teacherStore.save(new Teacher("John Doe", "john@school.com"));
        UUID randomStudentId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> studentService.assignStudentToTeacher(randomStudentId, teacher.getId()));
    }

    @Test
    void assignStudentToTeacher_teacherNotFound_throwsIllegalArgumentException() {
        InMemoryStudentStore studentStore = new InMemoryStudentStore();
        InMemoryTeacherStore teacherStore = new InMemoryTeacherStore();
        StudentService studentService = new StudentService(studentStore, teacherStore);

        Student student = studentService.createStudent("Anna Bellman", "anna@bellman.com", "piano");
        UUID randomTeacherId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> studentService.assignStudentToTeacher(student.getId(), randomTeacherId));
    }
}
