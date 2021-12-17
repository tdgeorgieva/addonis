package com.addonis.controllers.mvc;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.modelMappers.IdentityVerificationMapper;
import com.addonis.models.IdentityVerificationData;
import com.addonis.models.user.User;
import com.addonis.models.user.UserStatus;
import com.addonis.services.identityVerification.IdentityVerificationDataService;
import com.addonis.services.user.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/identityVerification")
public class IdentityVerificationMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final IdentityVerificationMapper modelMapper;
    private final IdentityVerificationDataService idService;

    @Autowired
    public IdentityVerificationMvcController(UserService userService, AuthenticationHelper authenticationHelper, IdentityVerificationMapper modelMapper, IdentityVerificationDataService idService) {
        super(userService);
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.idService = idService;
    }

    @GetMapping
    public String showVerificationPage(Model model) {
        List<User> usersForVerification = idService.getAll().stream()
                .map(IdentityVerificationData::getUser)
                .collect(Collectors.toList());

        model.addAttribute("usersForVerification", usersForVerification);
        return "id-verification-requests";
    }

    @GetMapping("/{username}")
    public String showUserRequest(@PathVariable String username, Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetUser(session);
        if (!currentUser.isAdmin()) {
            throw new UnauthorizedOperationException();
        }
        model.addAttribute("currentUser", currentUser);

        User userToShow = userService.getByUsername(username);
        IdentityVerificationData request = idService.getByUser(userToShow);

        String idCardImage = Base64.encodeBase64String(request.getIdCardPhoto());
        String selfie = Base64.encodeBase64String(request.getSelfiePhoto());

        model.addAttribute("user", userToShow);
        model.addAttribute("idCardImage", idCardImage);
        model.addAttribute("selfie", selfie);
        model.addAttribute("request", request);
        return "id-verification-request";
    }

    @PostMapping("/{username}")
    public String handleUserRequest(@RequestParam(required = false, value = "verify") String verify,
                                    @PathVariable String username,
                                    Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetUser(session);
        if (!currentUser.isAdmin()) {
            throw new UnauthorizedOperationException();
        }
        model.addAttribute("currentUser", currentUser);

        User requested = userService.getByUsername(username);
        IdentityVerificationData request = idService.getByUser(requested);

            if (verify != null) {
                requested.setStatus(UserStatus.VERIFIED);
                userService.update(requested);
            }
            idService.delete(request.getId());

        return "redirect:/users/{username}";
    }

}
