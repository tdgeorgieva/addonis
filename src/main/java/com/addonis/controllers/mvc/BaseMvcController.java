package com.addonis.controllers.mvc;

import com.addonis.exceptions.AuthenticationFailureException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.models.user.UserStatus;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@Component
public class BaseMvcController {

    public final UserService userService;

    @Autowired
    public BaseMvcController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUserUsername") != null;
    }

    @ModelAttribute("isVerified")
    public boolean populateIsVerified(HttpSession session) {
        return session.getAttribute("currentUserUsername") != null && userService.getByUsername(session.getAttribute("currentUserUsername").toString()).getStatus().equals(UserStatus.VERIFIED);
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        return session.getAttribute("currentUserUsername") != null && userService.getByUsername(session.getAttribute("currentUserUsername").toString()).isAdmin();
    }

    @ExceptionHandler({AuthenticationFailureException.class})
    public String authenticationFailureExceptionHandler() {
        return "redirect:/auth/login";
    }

    @ExceptionHandler({UnauthorizedOperationException.class})
    public String unauthorizedOperationExceptionHandler() {
        return "redirect:/auth/login";
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public String entityNotFoundExceptionHandler() {
        return "redirect:/not-found";
    }

    @ExceptionHandler({NullPointerException.class})
    public String nullPointerExceptionHandler() {
        return "redirect:/not-found";
    }
}
