package com.addonis.repositories.user;

import com.addonis.models.User;
import com.addonis.repositories.BaseCRUDRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseCRUDRepository<User> {

    List<User> search(Optional<String> search);

    void blockUser(int id, int adminId);
}
