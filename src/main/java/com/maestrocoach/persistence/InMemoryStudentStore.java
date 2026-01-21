package com.maestrocoach.persistence;

import com.maestrocoach.domain.Student;

import java.util.*;

public class InMemoryStudentStore {

    private final Map<UUID, Student> students;

    public InMemoryStudentStore() {
        this.students = new HashMap<>();
    }

    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public Optional<Student> findById(UUID id) {
        return Optional.ofNullable(students.get(id));
    }

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public List<Student> findByTeacherId(UUID teacherId) {
        List<Student> studentList = new ArrayList<>();

        for (Student student : students.values()) {
            if (Objects.equals(student.getTeacherId(), teacherId)) {
                studentList.add(student);
            }
        }

        return studentList;
    }

    public void deleteById(UUID id) {
        students.remove(id);
    }

    public void clear() {
        students.clear();
    }
}
