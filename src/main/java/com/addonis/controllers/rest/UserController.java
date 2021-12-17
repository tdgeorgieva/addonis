package com.addonis.controllers.rest;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.RegisterUserDto;
import com.addonis.dtos.UserDto;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.user.User;
import com.addonis.models.user.UserStatus;
import com.addonis.models.addon.Addon;
import com.addonis.services.addon.AddonService;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.addonis.controllers.Utils.setGitHubInformation;
import static com.addonis.services.Utils.throwIfNotAdmin;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final AddonService addonService;
    private final UserModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService service, AddonService addonService, UserModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.addonService = addonService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestParam(required = false) Optional<String> search) {
        return service.search(search);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("{id}/addons")
    public List<Addon> getAddons(@PathVariable int id) {
        User user = service.getById(id);
        List<Addon> list = addonService.getAddonsByUserId(user);
        for (Addon addon : list) {
            setGitHubInformation(addon);
        }
        return list;
    }

    @PutMapping("/{userId}/block")
    public User blockUser(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        User current = authenticationHelper.tryGetUser(headers);
        throwIfNotAdmin(current);

        User user = service.getById(userId);
        user.setStatus(UserStatus.BLOCKED);
        service.update(user);
        return user;
    }

    @PutMapping("/{userId}/unblock")
    public User unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int userId) {
        User current = authenticationHelper.tryGetUser(headers);
        throwIfNotAdmin(current);

        User user = service.getById(userId);
        user.setStatus(UserStatus.VERIFIED);
        service.update(user);
        return user;
    }

    @PutMapping("/{userId}/addPhoto")
    public void updateUserPhoto(@RequestHeader HttpHeaders headers, @PathVariable int userId, MultipartFile image) throws IOException {
        User current = authenticationHelper.tryGetUser(headers);
        throwIfNotAdmin(current);
        User user = service.getById(userId);

        if (!image.isEmpty()) {
            user.setPhoto(Base64.getEncoder().encode(image.getBytes()));
            service.update(user);
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody RegisterUserDto userDto) throws IOException {
        User user = modelMapper.fromDto(userDto);
        service.register(user);
        return user;
    }

    @PutMapping("/{id}")
    public User update(@PathVariable int id, @Valid @RequestBody UserDto userDto) throws IOException {
        User user = modelMapper.fromDto(userDto, id);
        service.update(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        service.delete(id);
    }
}