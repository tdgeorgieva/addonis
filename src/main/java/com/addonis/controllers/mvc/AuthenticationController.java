package com.addonis.controllers.mvc;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.LoginDto;
import com.addonis.dtos.RegisterUserDto;
import com.addonis.exceptions.AuthenticationFailureException;
import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.user.User;
import com.addonis.services.MailService;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends BaseMvcController {

    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;
    private final MailService mailService;

    @Autowired
    public AuthenticationController(UserService userService, ApplicationEventPublisher eventPublisher, AuthenticationHelper authenticationHelper, UserModelMapper modelMapper, MailService mailService) {
        super(userService);
        this.eventPublisher = eventPublisher;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("loginDto") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUserUsername", login.getUsername());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUserUsername");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("register", new RegisterUserDto());
        return "register";
    }

    @GetMapping("/successfulRegistration")
    public String showSuccessfulRegistrationPage(Model model, @ModelAttribute("currentUser") User user) {
        return "successful-registration";
    }

    @GetMapping("/confirm-account")
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken) {
        userService.confirmUserAccount(confirmationToken);
        return "confirm-account";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("register") RegisterUserDto registerUserDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            User user = modelMapper.fromDto(registerUserDto);
            userService.register(user);

            // add the default photo
            user.setPhoto(Files.readAllBytes(Paths.get("images/users/defaultProfilePhoto.png")));
            userService.update(user);

            model.addAttribute("currentUser", user);
            return "successful-registration";

        } catch (DuplicateEntityException | IOException e) {
            bindingResult.rejectValue("lastName", "exists", e.getMessage());
            return "register";
        }
    }


}
