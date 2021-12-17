package com.addonis.repositories.user;

import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.models.addon.Addon;
import com.addonis.repositories.BaseCRUDRepository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseCRUDRepository<User> {

    List<User> search(Optional<String> search);

    List<User> filter(String searchAll);

    List<Addon> getUserAddons(int id);

    Page<User> findAllWithFilters(UserPage userPage, UserSearchCriteria userSearchCriteria);
}
