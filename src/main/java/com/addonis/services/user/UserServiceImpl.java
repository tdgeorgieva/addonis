package com.addonis.services.user;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.User;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String ADMIN_ROLE = "admin";
    private static final String ADMIN_ERROR = "Only admins can block other users!";

    private final UserRepository userRepository;
//    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserModelMapper modelMapper) {
        this.userRepository = userRepository;
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
        boolean duplicateExists = true;
        try {
            userRepository.getByField("email", user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByField("email", user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

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
    public void blockUser(User user, User admin) {
        if (!admin.getRole().getName().equals(ADMIN_ROLE)) {
            throw new UnauthorizedOperationException(ADMIN_ERROR);
        }
        userRepository.blockUser(user.getId(), admin.getId());
    }

//    @Override
//    public User getUser(String verificationToken) {
//        User user = tokenRepository.findByToken(verificationToken).getUser();
//        return user;
//    }
//
//    @Override
//    public void saveRegisteredUser(User user) {
//        userRepository.update(user);
//    }
//
//    @Override
//    public void createVerificationToken(User user, String token) {
//        EmailVerificationToken myToken = new EmailVerificationToken(token, user);
//        tokenRepository.save(myToken);
//    }
//
//    @Override
//    public EmailVerificationToken getVerificationToken(String VerificationToken) {
//        return tokenRepository.findByToken(VerificationToken);
//    }

}
