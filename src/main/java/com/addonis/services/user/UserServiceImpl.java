package com.addonis.services.user;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.ConfirmationToken;
import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.models.user.UserStatus;
import com.addonis.models.addon.Addon;
import com.addonis.repositories.confirmationToken.ConfirmationTokenRepository;
import com.addonis.repositories.identityVerification.IdentityVerificationDataRepository;
import com.addonis.repositories.user.UserRepository;
import com.addonis.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;
    private final IdentityVerificationDataRepository idRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, MailService mailService, IdentityVerificationDataRepository idRepository) {
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mailService = mailService;
        this.idRepository = idRepository;
    }

    @Override
    public Page<User> getUsers(UserPage userPage, UserSearchCriteria userSearchCriteria) {
        return userRepository.findAllWithFilters(userPage, userSearchCriteria);
    }


    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByField("username", username);
    }

    @Override
    public void create(User user) {
        checkDuplicateExists("email", user.getEmail());
        checkDuplicateExists("username", user.getUsername());
        checkDuplicateExists("phoneNumber", user.getPhoneNumber());
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> search(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        return userRepository.search(search);
    }

    @Override
    public void blockUser(int userId, int adminId) {
        User blocked = getById(userId);
        blocked.setStatus(UserStatus.BLOCKED);
        update(blocked);
    }

    @Override
    public void unblockUser(int userId, int adminId) {
        User unblocked = getById(userId);
        unblocked.setStatus(UserStatus.VERIFIED);
        update(unblocked);
    }

    @Override
    public void followUser(int followerId, int followedId) {
        User follower = getById(followerId);
        User followed = getById(followedId);
        follower.follow(followed);
        followed.getFollowedBy(follower);
        update(follower);
    }

    @Override
    public void unfollowUser(int followerId, int followedId) {
        User follower = getById(followerId);
        User followed = getById(followedId);
        follower.unfollow(followed);
        followed.getUnfollowedBy(follower);
        //TODO db
        update(follower);
    }

    private void checkDuplicateExists(String attribute, String value) {
        boolean duplicateExists = true;
        try {
            userRepository.getByField(attribute, value);
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", attribute, value);
        }
    }

    @Override
    public void register(User user) {
        create(user);
        mailService.sendAccountVerificationMail(user);
    }

    @Override
    public void confirmUserAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = userRepository.getById(token.getUser().getId());
        user.setStatus(UserStatus.CONFIRMED);
        userRepository.update(user);
        confirmationTokenRepository.delete(token.getId());
    }

    @Override
    public List<User> filter(String searchAll) {
        return userRepository.filter(searchAll);
    }

    @Override
    public void inviteFriend(User inviter, String email) {
        mailService.sendInvitationMail(inviter, email);
    }

    @Override
    public List<Addon> getUserAddons(int userId) {
        return userRepository.getUserAddons(userId);
    }

    @Override
    public Set<User> getFollowing(User user) {
        return getById(user.getId()).getFollowing();
    }

    @Override
    public Set<User> getFollowers(User user) {
        return getById(user.getId()).getFollowers();
    }


    @Override
    public void updatePhoto(User user, byte[] photo) throws IOException {
        user.setPhoto(photo);
        userRepository.update(user);
    }

    @Override
    public boolean hasRequestedIdVerification(User user) {
        boolean has;
        try {
            idRepository.getByField("user.id", user.getId());
            has = true;
        } catch (EntityNotFoundException e) {
            has = false;
        }
        return has;
    }
}
