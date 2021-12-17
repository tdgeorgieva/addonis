package com.addonis.controllers.mvc;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.FilterUserDto;
import com.addonis.dtos.SearchAllDto;
import com.addonis.modelMappers.UserModelMapper;
import com.addonis.models.Review;
import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.models.addon.Addon;
import com.addonis.services.addon.AddonService;
import com.addonis.services.rating.RatingService;
import com.addonis.services.review.ReviewService;
import com.addonis.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

import static com.addonis.services.Utils.throwIfNotAdmin;

@Controller
@RequestMapping("/users")
public class UserMvcController extends BaseMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserModelMapper modelMapper;
    private final AddonService addonService;
    private final ReviewService reviewService;
    private final RatingService ratingService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserModelMapper modelMapper, AddonService addonService, ReviewService reviewService, RatingService ratingService) {
        super(userService);
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.addonService = addonService;
        this.reviewService = reviewService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public String showAllUsers(HttpSession session, UserPage page, UserSearchCriteria userSearchCriteria, Model model) {
        User admin = authenticationHelper.tryGetUser(session);
        throwIfNotAdmin(admin);
        model.addAttribute("currentUser", admin);

        Page<User> resp = userService.getUsers(page, userSearchCriteria);
        model.addAttribute("users", resp.stream().collect(Collectors.toList()));

        model.addAttribute("filterUserDto", new FilterUserDto(userSearchCriteria.getName(), page.getSortBy()));
        model.addAttribute("page", page);
        model.addAttribute("totalPages", resp.getTotalPages());
        List<String> queryParams = new ArrayList<>();
        if (page.getPageSize() != 0) {
            queryParams.add("pageSize=" + page.getPageSize());
        }
        if (userSearchCriteria.getName() != null && userSearchCriteria.getName().equals("")) {
            queryParams.add("name=" + userSearchCriteria.getName());
        }
        model.addAttribute("queryPath", String.join("&", queryParams));
        return "users";
    }

    @GetMapping("/{username}")
    public String showSingleUser(@PathVariable String username, Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetUser(session);
        model.addAttribute("currentUser", currentUser);

        User userToShow = userService.getByUsername(username);
        model.addAttribute("user", userToShow);

        List<Addon> userAddonsList = addonService.getAddonsByUserId(userToShow);
        model.addAttribute("userAddons", userAddonsList);

        HashMap<Integer, Double> addonsRatings = new HashMap<>();
        for (Addon addon : userAddonsList) {
            addonsRatings.put(addon.getId(), ratingService.calculateRating(addon.getId()));
        }

        List<Addon> userDraftsList = addonService.getDraftsByUserId(userToShow.getId());
        model.addAttribute("userDrafts", userDraftsList);

        List<Review> userReviewsList = reviewService.getByUser(userToShow.getId());
        model.addAttribute("userReviews", userReviewsList);

        model.addAttribute("addonsRatings", addonsRatings);

        return "user";
    }

    @GetMapping("/{username}/following")
    public String showFollowing(@PathVariable String username, Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetUser(session);
        User user = userService.getByUsername(username);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        Set<User> following = userService.getFollowing(user);
        model.addAttribute("following", following);

        return "following";
    }

    @GetMapping("/{username}/followers")
    public String showFollowers(@PathVariable String username, Model model, HttpSession session) {
        User currentUser = authenticationHelper.tryGetUser(session);
        User user = userService.getByUsername(username);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);

        Set<User> followers = userService.getFollowers(user);
        model.addAttribute("followers", followers);

        return "followers";
    }

    @GetMapping("{username}/follow")
    public String follow(@PathVariable String username, Model model, HttpSession session) {
        User follower = authenticationHelper.tryGetUser(session);
        User userToFollow = userService.getByUsername(username);

        model.addAttribute("currentUser", follower);
        model.addAttribute("user", userToFollow);

        userService.followUser(follower.getId(), userToFollow.getId());
        return "redirect:/users/{username}";
    }

    @GetMapping("{username}/unfollow")
    public String unfollow(@PathVariable String username, Model model, HttpSession session) {
        User follower = authenticationHelper.tryGetUser(session);
        User userToUnfollow = userService.getByUsername(username);

        model.addAttribute("currentUser", follower);
        model.addAttribute("user", userToUnfollow);

        userService.unfollowUser(follower.getId(), userToUnfollow.getId());
        return "redirect:/users/{username}";

    }

    @GetMapping("/{username}/block")
    public String blockUser(@PathVariable String username, Model model, HttpSession session) {
        User admin = authenticationHelper.tryGetUser(session);
        User userToBlock = userService.getByUsername(username);

        model.addAttribute("currentUser", admin);
        model.addAttribute("user", userToBlock);

        userService.blockUser(userToBlock.getId(), admin.getId());
        return "redirect:/users";
    }

    @GetMapping("/{username}/unblock")
    public String unblockUser(@PathVariable String username, Model model, HttpSession session) {
        User admin = authenticationHelper.tryGetUser(session);
        User userToUnblock = userService.getByUsername(username);

        model.addAttribute("currentUser", admin);
        model.addAttribute("user", userToUnblock);

        userService.unblockUser(userToUnblock.getId(), admin.getId());
        return "redirect:/users";
    }

    @PostMapping("")
    public String filter(@ModelAttribute SearchAllDto searchAllDto, Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("currentUser", user);

        List<User> userList = userService.filter(searchAllDto.getSearchAll());
        model.addAttribute("users", userList);
        return "users";
    }

}
