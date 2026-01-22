package com.maestrocoach.persistence;

import com.maestrocoach.domain.Teacher;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryTeacherStore {

    private final Map<UUID, Teacher> teachers;

    public InMemoryTeacherStore() {
        this.teachers = new HashMap<>();
    }

    public Teacher save(Teacher teacher) {
        teachers.put(teacher.getId(), teacher);
        return teacher;
    }

    public Optional<Teacher> findById(UUID id) {
        return Optional.ofNullable(teachers.get(id));
    }

    public List<Teacher> findAll() {
        return new ArrayList<>(teachers.values());
    }

    public void deleteById(UUID id) {
        teachers.remove(id);
    }

    public void clear() {
        teachers.clear();
    }
}
