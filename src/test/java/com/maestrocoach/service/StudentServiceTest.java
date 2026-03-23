package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Student;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.repository.StudentRepository;
import com.maestrocoach.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    @Test
    void createStudent_success() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(teacherRepository.save(Mockito.any(Teacher.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Student student = studentService.createStudent("Anna Bellman", "anna@bellman.com", "piano");

        assertNotNull(student);
        assertEquals("Anna Bellman", student.getFullName());
        assertEquals("anna@bellman.com", student.getEmail());
        assertEquals("piano", student.getInstrument());

        Mockito.verify(studentRepository).save(Mockito.any(Student.class));
    }

    @Test
    void assignStudentToTeacher_success() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(teacherRepository.save(Mockito.any(Teacher.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Teacher teacher = new Teacher("John Doe", "john@school.com");
        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));
        Mockito.when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));

        assertNull(student.getTeacher());

        studentService.assignStudentToTeacher(student.getId(), teacher.getId());

        assertNotNull(student.getTeacher());
        assertEquals(teacher.getId(), student.getTeacher().getId());

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(teacherRepository).findById(teacher.getId());
        Mockito.verify(studentRepository).save(Mockito.any(Student.class));
    }

    @Test
    void assignStudentToTeacher_studentNotFound_throwsResourceNotFoundException() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Teacher teacher = new Teacher("John Doe", "john@school.com");
        UUID randomStudentId = UUID.randomUUID();

        Mockito.when(studentRepository.findById(randomStudentId))
                .thenReturn(Optional.empty());

        Mockito.when(teacherRepository.findById(teacher.getId()))
                .thenReturn(Optional.of(teacher));

        assertThrows(ResourceNotFoundException.class, () -> studentService.assignStudentToTeacher(randomStudentId, teacher.getId()));

        Mockito.verify(studentRepository).findById(randomStudentId);
    }

    @Test
    void assignStudentToTeacher_teacherNotFound_throwsResourceNotFoundException() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Student student = new Student("Anna Bellman", "anna@bellman.com", "piano");
        UUID randomTeacherId = UUID.randomUUID();

        Mockito.when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));

        Mockito.when(teacherRepository.findById(randomTeacherId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.assignStudentToTeacher(student.getId(), randomTeacherId));

        Mockito.verify(studentRepository).findById(student.getId());
        Mockito.verify(teacherRepository).findById(randomTeacherId);
    }

    @Test
    void getStudentsByTeacher_teacherExists_returnsStudents() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Teacher teacher = new Teacher("John Doe", "john@school.com");
        UUID teacherId = teacher.getId();
        Student student1 = new Student("Anna Bellman", "anna@bellman.com", "piano");
        Student student2 = new Student("Charlie", "charlie@abc.com", "piano");

        Mockito.when(teacherRepository.findById(teacherId))
                .thenReturn(Optional.of(teacher));

        Mockito.when(studentRepository.findByTeacher_Id(teacherId))
                .thenReturn(List.of(student1, student2));

        List<Student> studentList = studentService.getStudentsByTeacher(teacherId);

        assertEquals(2, studentList.size());
        assertEquals("Anna Bellman", studentList.get(0).getFullName());
        assertEquals("anna@bellman.com", studentList.get(0).getEmail());
        assertEquals("piano", studentList.get(0).getInstrument());

        assertEquals("Charlie", studentList.get(1).getFullName());
        assertEquals("charlie@abc.com", studentList.get(1).getEmail());
        assertEquals("piano", studentList.get(1).getInstrument());

        Mockito.verify(teacherRepository).findById(teacherId);
        Mockito.verify(studentRepository).findByTeacher_Id(teacherId);
    }

    @Test
    void getStudentsByTeacher_teacherExistsButNoStudents_returnsEmptyList() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        Teacher teacher = new Teacher("John Doe", "john@school.com");
        UUID teacherId = teacher.getId();

        Mockito.when(teacherRepository.findById(teacherId))
                .thenReturn(Optional.of(teacher));

        Mockito.when(studentRepository.findByTeacher_Id(teacherId))
                .thenReturn(List.of());

        List<Student> studentList = studentService.getStudentsByTeacher(teacherId);

        assertTrue(studentList.isEmpty());

        Mockito.verify(teacherRepository).findById(teacherId);
        Mockito.verify(studentRepository).findByTeacher_Id(teacherId);
    }

    @Test
    void getStudentsByTeacher_teacherNotFound_throwsException() {
        StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
        TeacherRepository teacherRepository = Mockito.mock(TeacherRepository.class);

        StudentService studentService = new StudentService(studentRepository, teacherRepository);

        UUID randomId = UUID.randomUUID();

        Mockito.when(teacherRepository.findById(randomId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentsByTeacher(randomId));

        assertEquals("Teacher not found with id: " + randomId, ex.getMessage());

        Mockito.verify(studentRepository, Mockito.never()).findByTeacher_Id(Mockito.any());
    }
}
