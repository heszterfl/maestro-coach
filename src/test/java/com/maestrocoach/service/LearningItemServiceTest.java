package com.maestrocoach.service;

import com.maestrocoach.api.error.ResourceNotFoundException;
import com.maestrocoach.domain.LearningItem;
import com.maestrocoach.repository.LearningItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static com.maestrocoach.domain.LearningCategory.INSTRUMENT_PRACTICE;
import static org.junit.jupiter.api.Assertions.*;

public class LearningItemServiceTest {

    @Test
    void createLearningItem_success() {
        LearningItemRepository repository = Mockito.mock(LearningItemRepository.class);

        Mockito.when(repository.save(Mockito.any(LearningItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LearningItemService service = new LearningItemService(repository);

        LearningItem item = service.createLearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);

        assertNotNull(item);
        assertEquals("C-Dúr Etűd", item.getTitle());
        assertEquals(INSTRUMENT_PRACTICE, item.getCategory());
        assertNull(item.getDescription());

        Mockito.verify(repository).save(Mockito.argThat(i ->
                i.getTitle().equals("C-Dúr Etűd") &&
                i.getCategory().equals(INSTRUMENT_PRACTICE) &&
                i.getDescription() == null));
    }

    @Test
    void deleteLearningItem_success() {
        LearningItemRepository repository = Mockito.mock(LearningItemRepository.class);

        LearningItemService service = new LearningItemService(repository);

        LearningItem item = new LearningItem("C-Dúr Etűd", INSTRUMENT_PRACTICE, null);
        UUID itemId = item.getId();

        Mockito.when(repository.findById(itemId))
                .thenReturn(Optional.of(item));

        service.deleteLearningItem(item.getId());

        Mockito.verify(repository).findById(itemId);
        Mockito.verify(repository).delete(item);
    }

    @Test
    void deleteLearningItem_notFound() {
        LearningItemRepository repository = Mockito.mock(LearningItemRepository.class);

        LearningItemService service = new LearningItemService(repository);

        UUID randomId = UUID.randomUUID();

        Mockito.when(repository.findById(randomId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.deleteLearningItem(randomId));

        assertEquals("Learning item not found with id: " + randomId, ex.getMessage());

        Mockito.verify(repository).findById(randomId);
        Mockito.verify(repository, Mockito.never()).delete(Mockito.any(LearningItem.class));
    }
}
