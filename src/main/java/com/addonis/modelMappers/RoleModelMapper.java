package com.addonis.modelMappers;

import com.addonis.dtos.RoleDto;
import com.addonis.models.Role;
import com.addonis.repositories.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleModelMapper {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleModelMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role fromDto(RoleDto roleDto) {
        Role role = new Role();
        dtoToObject(roleDto, role);
        return role;
    }

    public Role fromDto(RoleDto roleDto, int id) {
        Role role = roleRepository.getById(id);
        dtoToObject(roleDto, role);
        return role;
    }

    private void dtoToObject(RoleDto roleDto, Role role) {
        role.setName(roleDto.getName());
    }
}
