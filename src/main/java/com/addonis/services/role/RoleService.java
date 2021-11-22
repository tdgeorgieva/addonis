package com.addonis.services.role;

import com.addonis.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Role getById(int id);

    void create(Role role);

    void update(Role role);

    void delete(int id);
}
