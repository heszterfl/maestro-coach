package com.maestrocoach.service;

import com.maestrocoach.domain.Teacher;
import com.maestrocoach.persistence.InMemoryTeacherStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherServiceTest {

    @Test
    void createTeacher_success() {
        InMemoryTeacherStore store = new InMemoryTeacherStore();
        TeacherService service = new TeacherService(store);

        Teacher teacher = service.createTeacher("John Doe", "john@school.com");

        assertNotNull(teacher);
        assertEquals("John Doe", teacher.getFullName());
        assertEquals("john@school.com", teacher.getEmail());
        assertEquals(1, store.findAll().size());
    }
}
