package com.addonis.services;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.IDE;
import com.addonis.repositories.IDE.IDERepository;
import com.addonis.services.IDE.IDEServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.addonis.Helpers.createMockIDE;

@ExtendWith(MockitoExtension.class)
public class IDEServiceTests {

    @Mock
    IDERepository mockRepository;

    @InjectMocks
    IDEServiceImpl service;

    @Test
    public void getAll_Should_Return_AllIdes() {
        var ide = createMockIDE();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(ide));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Ide_WhenMatchExists() {
        var ide = createMockIDE();

        Mockito.when(mockRepository.getById(ide.getId()))
                .thenReturn(ide);

        Assertions.assertDoesNotThrow(() -> service.getById(ide.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(ide.getId());
    }

    @Test
    public void create_Should_Throw_When_IdeNameIsTaken() {
        var ide = createMockIDE();
        Mockito.when(mockRepository.getByField("name", ide.getName()))
                .thenReturn(ide);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(ide));
    }

    @Test
    public void create_ShouldCallRepository_When_IdeDoesNotExists() {
        var ide = createMockIDE();

        Mockito.when(mockRepository.getByField("name", ide.getName()))
                .thenThrow(new EntityNotFoundException("Ide", "name", ide.getName()));

        Assertions.assertDoesNotThrow(() -> service.create(ide));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(IDE.class));
    }

    @Test
    public void update_Should_Throw_When_IdeNameIsTaken() {
        var ide = createMockIDE();

        Mockito.when(mockRepository.getByField("name", ide.getName()))
                .thenReturn(ide);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(ide));
    }

    @Test
    public void update_Should_Throw_When_IdeNameDoesNotExists() {
        var ide = createMockIDE();

        Mockito.when(mockRepository.getByField("name", ide.getName()))
                .thenThrow(new EntityNotFoundException("Ide", "name", ide.getName()));

        Assertions.assertDoesNotThrow(() -> service.update(ide));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(IDE.class));
    }

    @Test
    public void delete_Should_Delete_Ide() {
        var ide = createMockIDE();

        Assertions.assertDoesNotThrow(() -> service.delete(ide.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(ide.getId());
    }
}
