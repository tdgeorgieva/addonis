package com.addonis.controllers.rest;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.RegisterUserDto;
import com.addonis.dtos.UserDto;
import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.User;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserModelMapper modelMapper;

    @Autowired
    public UserController(UserService service, UserModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }


    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody RegisterUserDto userDto) {
        try {
            User user = modelMapper.fromDto(userDto);
            service.create(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = modelMapper.fromDto(userDto, id);
            service.update(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}