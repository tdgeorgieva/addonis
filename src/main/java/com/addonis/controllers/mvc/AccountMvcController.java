package com.addonis.controllers.mvc;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.EmailDto;
import com.addonis.dtos.IdentityVerificationDto;
import com.addonis.dtos.UserDto;
import com.addonis.dtos.UserPasswordDto;
import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityUpdateException;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.modelMappers.IdentityVerificationMapper;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.IdentityVerificationData;
import com.addonis.models.user.User;
import com.addonis.services.identityVerification.IdentityVerificationDataService;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/account")
public class AccountMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;
    private final IdentityVerificationMapper idModelMapper;
    private final IdentityVerificationDataService idService;

    @Autowired
    public AccountMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserModelMapper modelMapper, IdentityVerificationMapper idModelMapper, IdentityVerificationDataService idService) {
        super(userService);
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.idModelMapper = idModelMapper;
        this.idService = idService;
    }

    @GetMapping
    public String showAccountPage(Model model, HttpSession session) {
        try {
            User user = userService.getByUsername(session.getAttribute("currentUserUsername").toString());
            model.addAttribute("user", user);

        } catch (NullPointerException e) {
            return "redirect:/auth/login";
        }
        return "account";
    }

    @GetMapping("/update")
    public String showEditAccountPage(Model model, HttpSession session) throws IOException {
        User user = authenticationHelper.tryGetUser(session);
        UserDto userDto = modelMapper.toDto(user);

        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("userDto", userDto);

        return "account-update";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("userDto") UserDto dto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "account-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            User updated = modelMapper.fromDto(dto, user.getId());
            userService.update(updated);

            return "redirect:/account";
        } catch (EntityUpdateException ignored) {
            return "redirect:/account";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate", e.getMessage());
            return "account-update";
        } catch (IOException e) {
            return "account-update";
        }
    }

    @GetMapping("/updatePassword")
    public String updatePasswordPage(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        UserPasswordDto userPasswordDto = new UserPasswordDto();

        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("userPasswordDto", userPasswordDto);

        return "account-update-password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("userPasswordDto") UserPasswordDto dto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "account-update-password";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            User updated = modelMapper.fromDto(dto, user.getId());
            userService.update(updated);
            return "redirect:/account";

        } catch (UnauthorizedOperationException e) {
            errors.rejectValue("oldPassword", "no_match", e.getMessage());
            return "account-update-password";
        }
    }

    @GetMapping("/requestVerification")
    public String requestVerificationPage(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("user", user);

        boolean hasAlreadyRequested = userService.hasRequestedIdVerification(user);
        model.addAttribute("hasRequestedVerification", hasAlreadyRequested);

        model.addAttribute("verificationDto", new IdentityVerificationDto());
        return "verify-identity";
    }

    @PostMapping("/requestVerification")
    public String requestVerification(@Valid @ModelAttribute("verificationDto") IdentityVerificationDto dto,
                                      BindingResult errors,
                                      Model model,
                                      HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "verify-identity";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            IdentityVerificationData data = idModelMapper.fromDto(dto);
            data.setUser(user);
            idService.create(data);

            return "redirect:/account/requestVerification";
        } catch (IOException e) {
            return "verify-identity";
        }
    }

    @GetMapping("/invite")
    public String showInviteFriendPage(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("user", user);
        model.addAttribute("emailDto", new EmailDto());

        return "invite-friend";
    }

    @PostMapping("/invite")
    public String inviteFriend(@Valid @ModelAttribute("emailDto") EmailDto emailDto, Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("user", user);
        String email = emailDto.getEmail();
        userService.inviteFriend(user, email);

        return "invite-friend";
    }
}
