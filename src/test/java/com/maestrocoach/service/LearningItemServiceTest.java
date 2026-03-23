package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.persistence.InMemoryLearningItemStore;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
import static org.junit.jupiter.api.Assertions.*;

public class LearningItemServiceTest {

    @Test
    void createLearningItem_success() {
        InMemoryLearningItemStore store = new InMemoryLearningItemStore();
        LearningItemService service = new LearningItemService(store);

        LearningItem item = service.createLearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        assertNotNull(item);
        assertEquals("C-Dúr Etűd", item.getTitle());
        assertEquals(INSTRUMENT_PRACTICE, item.getCategory());
        assertNull(item.getDescription());
        assertEquals(1, store.findAll().size());
    }

    @Test
    void deleteLearningItem_success() {
        InMemoryLearningItemStore store = new InMemoryLearningItemStore();
        LearningItemService service = new LearningItemService(store);

        LearningItem item = service.createLearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        assertEquals(1, store.findAll().size());

        service.deleteLearningItem(item.getId());

        assertEquals(0, store.findAll().size());
        assertTrue(store.findById(item.getId()).isEmpty());
    }

    @Test
    void deleteLearningItem_notFound() {
        InMemoryLearningItemStore store = new InMemoryLearningItemStore();
        LearningItemService service = new LearningItemService(store);

        UUID randomId = UUID.randomUUID();

        assertThrows(ResourceNotFoundException.class, () -> service.deleteLearningItem(randomId));
    }
}
