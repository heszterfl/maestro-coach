package com.maestrocoach.repository;

import com.maestrocoach.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}
