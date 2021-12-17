package com.addonis.services;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.repositories.identityVerification.IdentityVerificationDataRepository;
import com.addonis.repositories.user.UserRepository;
import com.addonis.services.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.addonis.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;
    @Mock
    IdentityVerificationDataRepository mockIdRepository;

    @InjectMocks
    UserServiceImpl service;

    private static final String filter = "filter";
    private static final String mockPhoto = "mockPhoto";

    @Test
    public void getUsers_Should_Return_Users() {
        UserPage mockUserPage = new UserPage();
        UserSearchCriteria mockCriteria = new UserSearchCriteria();

        Assertions.assertDoesNotThrow(() -> service.getUsers(mockUserPage, mockCriteria));

        Mockito.verify(mockRepository, Mockito.times(1))
                .findAllWithFilters(mockUserPage, mockCriteria);
    }

    @Test
    public void getAll_Should_Return_AllUsers() {
        var user = createMockUser();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(user));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_User_WhenMatchExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);

        Assertions.assertDoesNotThrow(() -> service.getById(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(user.getId());
    }

    @Test
    public void getByUsername_Should_Return_User_WhenMatchExists() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByField("username", user.getUsername()))
                .thenReturn(user);

        Assertions.assertDoesNotThrow(() -> service.getByUsername(user.getUsername()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByField("username", user.getUsername());
    }

    @Test
    public void create_Should_Throw_When_UserNameIsTaken() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByField("email", user.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", user.getEmail()));
        Mockito.when(mockRepository.getByField("username", user.getUsername()))
                .thenReturn(user);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_Should_Throw_When_EmailIsTaken() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByField("email", user.getEmail()))
                .thenReturn(user);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_Should_Throw_When_PhoneNumberIsTaken() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByField("username", user.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", user.getUsername()));
        Mockito.when(mockRepository.getByField("email", user.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", user.getEmail()));
        Mockito.when(mockRepository.getByField("phoneNumber", user.getPhoneNumber()))
                .thenReturn(user);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(user));
    }

    @Test
    public void create_ShouldCallRepository_When_UserDoesNotExist() {
        var user = createMockUser();

        Mockito.when(mockRepository.getByField("email", user.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "email", user.getEmail()));
        Mockito.when(mockRepository.getByField("username", user.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", user.getUsername()));
        Mockito.when(mockRepository.getByField("phoneNumber", user.getPhoneNumber()))
                .thenThrow(new EntityNotFoundException("User", "phoneNumber", user.getPhoneNumber()));

        Assertions.assertDoesNotThrow(() -> service.create(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(User.class));
    }

    @Test
    public void update_Should_Update_User() {
        var user = createMockUser();

        Assertions.assertDoesNotThrow(() -> service.update(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void delete_Should_Delete_User() {
        var user = createMockUser();

        Assertions.assertDoesNotThrow(() -> service.delete(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(user.getId());
    }

    @Test
    public void search_Should_Return_AllUsers_When_SearchIsEmpty() {

        Assertions.assertDoesNotThrow(() -> service.search(Optional.empty()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void blockUser_Should_Update_User() {
        var user = createMockUser();
        var admin = createMockAdmin();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);
        Assertions.assertDoesNotThrow(() -> service.blockUser(user.getId(), admin.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void unblockUser_Should_Update_User() {
        var user = createMockUser();
        var admin = createMockAdmin();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);
        Assertions.assertDoesNotThrow(() -> service.unblockUser(user.getId(), admin.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void followUser_Should_Update_User() {
        var user = createMockUser();
        var otherUser = createMockUser();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);
        Mockito.when(mockRepository.getById(otherUser.getId()))
                .thenReturn(otherUser);
        Assertions.assertDoesNotThrow(() -> service.followUser(user.getId(), otherUser.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void unfollowUser_Should_Update_User() {
        var user = createMockUser();
        var otherUser = createMockUser();

        Mockito.when(mockRepository.getById(user.getId()))
                .thenReturn(user);
        Mockito.when(mockRepository.getById(otherUser.getId()))
                .thenReturn(otherUser);
        Assertions.assertDoesNotThrow(() -> service.unfollowUser(user.getId(), otherUser.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void filter_ShouldCallRepository() {

        Assertions.assertDoesNotThrow(() -> service.filter(filter));

        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(filter);
    }

    @Test
    public void getUserAddons_ShouldCallRepository() {
        Assertions.assertDoesNotThrow(() -> service.getUserAddons(1));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getUserAddons(1);
    }

    @Test
    public void updatePhoto_Should_Update_User() {
        var user = createMockUser();

        byte[] photo = mockPhoto.getBytes(StandardCharsets.UTF_8);
        Assertions.assertDoesNotThrow(() -> service.updatePhoto(user, photo));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void hasRequestedIdVerification_Should_ReturnFalse_WhenUserHasNotRequested() {
        var user = createMockUser();
        var idData = createMockIdVerificationData();

        Mockito.when(mockIdRepository.getByField("user.id", user.getId()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertFalse(service.hasRequestedIdVerification(user));
    }

    @Test
    public void hasRequestedIdVerification_Should_ReturnTrue_WhenUserHasRequested() {
        var user = createMockUser();
        var idData = createMockIdVerificationData();

        Mockito.when(mockIdRepository.getByField("user.id", user.getId()))
                .thenReturn(idData);

        Mockito.lenient().when(service.hasRequestedIdVerification(user)).thenReturn(true);
    }
}
