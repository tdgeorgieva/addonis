package com.addonis.services;

import com.addonis.models.IdentityVerificationData;
import com.addonis.repositories.identityVerification.IdentityVerificationDataRepository;
import com.addonis.services.identityVerification.IdentityVerificationDataServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.addonis.Helpers.createMockIdVerificationData;

@ExtendWith(MockitoExtension.class)
public class IdVerificationDataServiceTests {

    @Mock
    IdentityVerificationDataRepository mockRepository;

    @InjectMocks
    IdentityVerificationDataServiceImpl service;

    @Test
    public void getAll_Should_Return_AllIdVerificationData() {
        var data = createMockIdVerificationData();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(data));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_Data_WhenMatchExists() {
        var data = createMockIdVerificationData();

        Mockito.when(mockRepository.getById(data.getId()))
                .thenReturn(data);

        Assertions.assertDoesNotThrow(() -> service.getById(data.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(data.getId());
    }

    @Test
    public void getByUser_Should_Return_Data_WhenMatchExists() {
        var data = createMockIdVerificationData();

        Mockito.when(mockRepository.getByField("user.id", data.getUser().getId()))
                .thenReturn(data);

        Assertions.assertDoesNotThrow(() -> service.getByUser(data.getUser()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByField("user.id", data.getUser().getId());
    }

    @Test
    public void create_ShouldCallRepository() {
        var data = createMockIdVerificationData();

        Assertions.assertDoesNotThrow(() -> service.create(data));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(IdentityVerificationData.class));
    }


    @Test
    public void update_create_ShouldCallRepository() {
        var data = createMockIdVerificationData();

        Assertions.assertDoesNotThrow(() -> service.update(data));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(IdentityVerificationData.class));
    }

    @Test
    public void delete_Should_Delete_Data() {
        var data = createMockIdVerificationData();

        Assertions.assertDoesNotThrow(() -> service.delete(data.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(data.getId());
    }
}
