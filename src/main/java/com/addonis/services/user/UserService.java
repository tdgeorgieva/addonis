package com.addonis.services.user;

import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.models.addon.Addon;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> search(Optional<String> search);

    void blockUser(int userId, int adminId);

    void unblockUser(int userId, int adminId);

    void followUser(int followerId, int followedId);

    void unfollowUser(int followerId, int followedId);

    void register(User user);

    void confirmUserAccount(String confirmationToken);

    List<User> filter(String searchAll);

    void inviteFriend(User inviter, String email);

    List<Addon> getUserAddons(int userId);

    Set<User> getFollowing(User user);

    Set<User> getFollowers(User user);

    void updatePhoto(User user, byte[] photo) throws IOException;

    boolean hasRequestedIdVerification(User user);

    Page<User> getUsers(UserPage userPage, UserSearchCriteria userSearchCriteria);

}
