package com.addonis.services.user;

import com.addonis.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);

    void delete(int id);

    List<User> search(Optional<String> search);

    void blockUser(User user, User admin);

//    User getUser(String verificationToken);
//
//    void saveRegisteredUser(User user);
//
//    void createVerificationToken(User user, String token);
//
//    EmailVerificationToken getVerificationToken(String VerificationToken);
}
