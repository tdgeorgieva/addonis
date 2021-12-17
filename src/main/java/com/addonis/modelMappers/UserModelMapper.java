package com.addonis.modelMappers;

import com.addonis.dtos.RegisterUserDto;
import com.addonis.dtos.UserDto;
import com.addonis.dtos.UserPasswordDto;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.models.user.User;
import com.addonis.models.user.UserStatus;
import com.addonis.repositories.role.RoleRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class UserModelMapper {
    private static final String AUTHENTICATION_FAILURE_MESSAGE = "Incorrect current password!";
    private final int USER_ROLE = 2;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserModelMapper(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User fromDto(RegisterUserDto registerUserDto) throws IOException {
        User user = new User();
        registerDtoToObject(registerUserDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, int id) throws IOException {
        User user = userRepository.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserPasswordDto dto, int id) {
        User user = userRepository.getById(id);
        passwordDtoToObject(dto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) throws IOException {
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        if (userDto.getFile() != null) {
            user.setPhoto(userDto.getFile().getBytes());
        }
    }

    private void registerDtoToObject(RegisterUserDto registerUserDto, User user) throws IOException {
        user.setRole(roleRepository.getById(USER_ROLE));
        user.setStatus(UserStatus.NOT_CONFIRMED);
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
    }

    private void passwordDtoToObject(UserPasswordDto dto, User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedOperationException(AUTHENTICATION_FAILURE_MESSAGE);
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    public UserDto toDto(User user) throws IOException {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setFile(convertUserImageToMultipartFile(user));

        if (user.getFirstName() != null) {
            userDto.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userDto.setLastName(user.getLastName());
        }
        return userDto;
    }

    private MultipartFile convertUserImageToMultipartFile(User user) {
        File file = new File("images/users/multipartFileImage");
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", user.getPhoto());
        return multipartFile;
    }
}
