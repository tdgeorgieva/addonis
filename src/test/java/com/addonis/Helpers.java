package com.addonis;

import com.addonis.models.IDE;
import com.addonis.models.Review;
import com.addonis.models.IdentityVerificationData;
import com.addonis.models.Role;
import com.addonis.models.addon.AddonCode;
import com.addonis.models.addon.Rating;
import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;
import com.addonis.models.user.UserStatus;

import java.util.HashSet;
import com.addonis.models.user.UserStatus;
import com.addonis.services.MailServiceImpl;

import java.time.LocalDate;

public class Helpers {

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mockUserEmail");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastname");
        mockUser.setPassword("mockPassword");
        mockUser.setPhoneNumber("mockPhoneNumber");
        mockUser.setRole(createMockUserRole());
        mockUser.setStatus(UserStatus.CONFIRMED);
        mockUser.setFollowers(new HashSet<>());
        mockUser.getFollowers().add(createMockAdmin());
        mockUser.setFollowing(new HashSet<>());
        mockUser.getFollowing().add(createMockAdmin());
        return mockUser;
    }

    public static User createBlockUser() {
        var mockUser = createMockUser();
        mockUser.setStatus(UserStatus.BLOCKED);
        return mockUser;
    }

    public static IdentityVerificationData createMockIdVerificationData() {
        var mock = new IdentityVerificationData();
        mock.setId(1);
        mock.setUser(createMockUser());
        mock.setIdCardPhoto("photo".getBytes());
        mock.setIdCardPhoto("photo".getBytes());
        return mock;
    }

    public static User createMockAdmin() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mockAdminEmail");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastname");
        mockUser.setPassword("mockPassword");
        mockUser.setPhoneNumber("mockPhoneNumber");
        mockUser.setRole(createMockAdminRole());
        return mockUser;
    }

    public static Role createMockUserRole() {
        var mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("user");
        return mockRole;
    }

    public static Role createMockAdminRole() {
        var mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("admin");
        return mockRole;
    }

    public static IDE createMockIDE() {
        var mockIde = new IDE();
        mockIde.setId(1);
        mockIde.setName("IntelliJ");
        return mockIde;
    }

    public static Rating createMockRating() {
        var mockRating = new Rating();
        mockRating.setId(1);
        mockRating.setRating(1);
        mockRating.setAddon(createMockAddon());
        mockRating.setUser(createMockUser());
        return mockRating;
    }

    public static Review createMockReview() {
        var mockReview = new Review();
        mockReview.setId(1);
        mockReview.setUser(createMockUser());
        mockReview.setAddon(createMockAddon());
        mockReview.setDescription("test");
        mockReview.setDate(LocalDate.now());
        return mockReview;
    }

    public static Addon createMockAddon() {
        var mockAddon = new Addon();
        mockAddon.setId(1);
        mockAddon.setName("mockAddonName");
        mockAddon.setUser(createMockUser());
        mockAddon.setDescription("mockDescription");
        mockAddon.setIde(createMockIDE());
        return mockAddon;
    }
    public static AddonCode createMockCode() {
        var mockCode = new AddonCode();
        mockCode.setId(1);
        mockCode.setCode("123");
        mockCode.setAddon(createMockAddon());
        mockCode.setExpirationDate(LocalDate.now());
        return mockCode;
    }

}
