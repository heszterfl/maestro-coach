package com.maestrocoach.repository;

import com.maestrocoach.domain.LearningItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LearningItemRepository extends JpaRepository<LearningItem, UUID> {
}
