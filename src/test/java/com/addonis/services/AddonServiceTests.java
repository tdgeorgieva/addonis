package com.addonis.services;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.exceptions.InvalidCodeException;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.models.addon.*;
import com.addonis.repositories.addon.AddonCodeRepository;
import com.addonis.repositories.addon.AddonRepository;
import com.addonis.repositories.review.ReviewRepository;
import com.addonis.services.addon.AddonServiceImpl;
import com.addonis.services.review.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static com.addonis.Helpers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class AddonServiceTests {

    @Mock
    AddonRepository mockRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    AddonCodeRepository codeRepository;

    @Mock
    MailService mailService;

    @InjectMocks
    AddonServiceImpl service;

    @Test
    public void getAll_Should_Return_AllAddons() {
        var addon = createMockAddon();
        Mockito.when(mockRepository.getAll())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getAll());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getAddons_Should_Return_AllAddons() {
        AddonPage page = new AddonPage();
        AddonSearchCriteria search = new AddonSearchCriteria();

        Assertions.assertDoesNotThrow(() -> service.getAddons(page, search, true));

        Assertions.assertDoesNotThrow(() -> mockRepository.findAllWithFilters(page, search, true));
    }

    @Test
    public void getById_Should_Return_Addon_WhenMatchExists() {
        var addon = createMockAddon();
        Mockito.when(mockRepository.getById(addon.getId()))
                .thenReturn(addon);

        Assertions.assertDoesNotThrow(() -> service.getById(addon.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getById(addon.getId());
    }

    @Test
    public void create_Should_Throw_When_AddonNameIsTaken() {
        var addon = createMockAddon();
        var admin = createMockAdmin();
        Mockito.when(mockRepository.getByField("name", addon.getName()))
                .thenReturn(addon);
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(addon, admin));
    }

    @Test
    public void create_Should_Throw_When_EntityNotFound() {
        var addon = createMockAddon();
        Mockito.when(mockRepository.getByField("name", addon.getName()))
                .thenThrow(new EntityNotFoundException("Addon", "name", "test"));
        Assertions.assertDoesNotThrow(() -> service.create(addon, createMockUser()));
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(addon);
        Mockito.verify(mailService, Mockito.times(1))
                .sendAddonVerificationCode(addon, createMockUser());
    }


    @Test
    public void delete_Should_Delete_Addon() {
        var addon = createMockAddon();
        var admin = createMockAdmin();
        var blockedUser = createBlockUser();
        var review = createMockReview();

        Mockito.when(mockRepository.getById(addon.getId()))
                .thenReturn(addon);

        Assertions.assertDoesNotThrow(() -> reviewRepository.deleteByAddon(addon.getId()));
        Assertions.assertDoesNotThrow(() -> service.delete(addon.getId(), admin));
        Assertions.assertThrows(UnauthorizedOperationException.class, () -> service.delete(addon.getId(), blockedUser));

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(addon.getId());
    }

    @Test
    public void getByName_Should_Return_Addon_WhenMatchExists() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.getByField("name", addon.getName()))
                .thenReturn(addon);

        Assertions.assertDoesNotThrow(() -> service.getByName(addon.getName()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getByField("name", addon.getName());
    }

    @Test
    public void update_Should_Throw_When_UserNotAdminOrOwner() {
        var addon = createMockAddon();
        var user = createMockAdmin();

        Assertions.assertDoesNotThrow(() -> service.update(addon, user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Addon.class));
    }

    @Test
    public void filter_Should_FilterAddons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.filter(Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.filter(Optional.empty(), Optional.empty(), Optional.empty()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test
    public void sortAddonsByDownloads_Should_Sort_Addons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.sortAddonsByDownloads())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.sortAddonsByDownloads());

        Mockito.verify(mockRepository, Mockito.times(1))
                .sortAddonsByDownloads();
    }

    @Test
    public void getFeaturedAddons_Should_Get_Featured_Addons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.getFeaturedAddons())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getFeaturedAddons());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getFeaturedAddons();
    }

    @Test
    public void getUnapprovedAddons_Should_Get_Unapproved_Addons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.getUnapprovedAddons())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getUnapprovedAddons());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getUnapprovedAddons();
    }

    @Test
    public void getPopularAddons_Should_Get_Popular_Addons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.getPopularAddons())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getPopularAddons());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getPopularAddons();
    }

    @Test
    public void getNewAddons_Should_Get_New_Addons() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.getNewAddons())
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getNewAddons());

        Mockito.verify(mockRepository, Mockito.times(1))
                .getNewAddons();
    }

    @Test
    public void sortAddonsByLastCommitDate_Should_Sort_Addons_ByLastCommitDate() {
        var addon = createMockAddon();

        Mockito.when(mockRepository.sortAddonsByLastCommitDate(List.of(addon)))
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.sortAddonsByLastCommitDate(List.of(addon)));

        Mockito.verify(mockRepository, Mockito.times(1))
                .sortAddonsByLastCommitDate(List.of(addon));
    }

    @Test
    public void getDraftsByUserId_Should_Return_DraftAddons() {

        var addon = createMockAddon();
        var user = createMockUser();

        Mockito.when(mockRepository.getDraftsByUserId(user.getId()))
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getDraftsByUserId(user.getId()));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getDraftsByUserId(user.getId());
    }

    @Test
    public void getAddonsByUserId_Should_Return_UserAddons() {

        var addon = createMockAddon();
        var user = createMockUser();

        Mockito.when(mockRepository.getAddonsByUserId(user.getId()))
                .thenReturn(List.of(addon));

        Assertions.assertDoesNotThrow(() -> service.getAddonsByUserId(user));

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAddonsByUserId(user.getId());
    }

    @Test
    public void approve_Should_Approve_Addon() {

        var addon = createMockAddon();
        addon.setStatus(AddonStatus.APPROVED);

        Assertions.assertDoesNotThrow(() -> service.approve(addon, createMockAdmin()));
    }

    @Test
    public void feature_Should_Mark_Addon_AsFeatured() {

        var addon = createMockAddon();
        addon.setFeatured(true);

        Assertions.assertDoesNotThrow(() -> service.feature(addon, createMockAdmin()));
    }

    @Test
    public void verifyAddon_Should_Throw_When_CodeInvalid() {
        var code = createMockCode();
        Mockito.when(mockRepository.getById(code.getAddon().getId()))
                        .thenThrow(new EntityNotFoundException(""));
        Mockito.when(codeRepository.findByCode(code.getCode()))
                        .thenReturn(code);
        Assertions.assertThrows(InvalidCodeException.class, () -> service.verifyAddon("123", createMockUser()));

    }

    @Test
    public void verifyAddon_Should_Throw() {
        var user = createMockUser();
        var code = createMockCode();
        var addon = createMockAddon();
        user.setUsername("test");
        Mockito.when(mockRepository.getById(code.getAddon().getId()))
                .thenReturn(addon);
        Mockito.when(codeRepository.findByCode(code.getCode()))
                .thenReturn(code);
        Assertions.assertThrows(InvalidCodeException.class, () -> service.verifyAddon("123", user));

    }
    @Test
    public void verifyAddon_Should_VerifyAddon() {
        var addon = createMockAddon();
        addon.setStatus(AddonStatus.VERIFIED);
        var code = createMockCode();
        Mockito.when(mockRepository.getById(code.getAddon().getId()))
                .thenReturn(addon);
        Mockito.when(codeRepository.findByCode(code.getCode()))
                .thenReturn(code);
        Assertions.assertDoesNotThrow(() -> service.verifyAddon(code.getCode(), createMockUser()));
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(addon);
        Mockito.verify(codeRepository, Mockito.times(1))
                .delete(code.getId());
    }
}