package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.Teacher;
import com.maestrocoach.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherServiceTest {

    @Test
    void createTeacher_success() {
        TeacherRepository repository = Mockito.mock(TeacherRepository.class);

        Mockito.when(repository.save(Mockito.any(Teacher.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TeacherService service = new TeacherService(repository);

        Teacher teacher = service.createTeacher("John Doe", "john@school.com");

        assertNotNull(teacher);
        assertEquals("John Doe", teacher.getFullName());
        assertEquals("john@school.com", teacher.getEmail());

        Mockito.verify(repository).save(Mockito.argThat(t ->
                t.getFullName().equals("John Doe") &&
                t.getEmail().equals("john@school.com")));
    }

    @Test
    void getTeacherById_teacherExists_returnsTeacher() {
        TeacherRepository repository = Mockito.mock(TeacherRepository.class);

        TeacherService service = new TeacherService(repository);

        Teacher teacher = new Teacher("John Doe", "john@school.com");
        UUID teacherId = teacher.getId();

        Mockito.when(repository.findById(teacherId))
                .thenReturn(Optional.of(teacher));

        Teacher queried = service.getTeacherById(teacherId);

        assertEquals("John Doe", queried.getFullName());
        assertEquals("john@school.com", queried.getEmail());

        Mockito.verify(repository).findById(teacherId);
    }

    @Test
    void getTeacherById_teacherNotFound_throws() {
        TeacherRepository repository = Mockito.mock(TeacherRepository.class);

        TeacherService service = new TeacherService(repository);

        UUID randomId = UUID.randomUUID();

        Mockito.when(repository.findById(randomId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.getTeacherById(randomId));

        assertEquals("Teacher not found with id: " + randomId, ex.getMessage());

        Mockito.verify(repository).findById(randomId);
    }
}
