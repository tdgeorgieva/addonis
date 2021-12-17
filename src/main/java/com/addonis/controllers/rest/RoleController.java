package com.addonis.controllers.rest;

import com.addonis.dtos.RoleDto;
import com.addonis.modelMappers.RoleModelMapper;
import com.addonis.models.Role;
import com.addonis.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleModelMapper roleModelMapper;
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleModelMapper roleModelMapper, RoleService roleService) {
        this.roleModelMapper = roleModelMapper;
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable int id) {
        return roleService.getById(id);
    }

    @PostMapping
    public Role create(@Valid @RequestBody RoleDto roleDto) {
        Role role = roleModelMapper.fromDto(roleDto);
        roleService.create(role);
        return role;
    }

    @PutMapping("/{id}")
    public Role update(@PathVariable int id, @Valid @RequestBody RoleDto roleDto) {
        Role role = roleModelMapper.fromDto(roleDto, id);
        roleService.update(role);
        return role;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        roleService.delete(id);

    }
}
