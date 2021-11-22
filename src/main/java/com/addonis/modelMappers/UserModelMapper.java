package com.addonis.modelMappers;

import com.addonis.dtos.RegisterUserDto;
import com.addonis.dtos.UserDto;
import com.addonis.models.User;
import com.addonis.repositories.role.RoleRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {
    private final int USER_ROLE = 2;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserModelMapper(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User fromDto(RegisterUserDto registerUserDto) {
        User user = new User();
        registerDtoToObject(registerUserDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, int id) {
        User user = userRepository.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) {
        user.setRole(roleRepository.getById(USER_ROLE));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(user.getRole());
        user.setPhoto(userDto.getPhoto());
        user.setStatus(userDto.getStatus());
    }

    private void registerDtoToObject(RegisterUserDto registerUserDto, User user) {
        user.setRole(roleRepository.getById(USER_ROLE));
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setPassword(registerUserDto.getPassword());
    }
}
