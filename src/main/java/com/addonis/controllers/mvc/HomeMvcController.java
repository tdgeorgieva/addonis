package com.addonis.controllers.mvc;


import com.addonis.dtos.FilterAddonDto;
import com.addonis.dtos.SearchAllDto;
import com.addonis.models.IDE;
import com.addonis.models.addon.Addon;
import com.addonis.services.IDE.IDEService;
import com.addonis.services.addon.AddonService;
import com.addonis.services.rating.RatingService;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeMvcController extends BaseMvcController {

    private final AddonService addonService;
    private final IDEService ideService;
    private final RatingService ratingService;

    @Autowired
    public HomeMvcController(UserService userService, AddonService addonService, IDEService ideService, RatingService ratingService) {
        super(userService);
        this.addonService = addonService;
        this.ideService = ideService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("searchAllDto", new SearchAllDto());
        model.addAttribute("featuredAddons", addonService.getFeaturedAddons());
        model.addAttribute("newAddons", addonService.getNewAddons());
        model.addAttribute("popularAddons", addonService.getPopularAddons());
        HashMap<Integer, Double> addonsRatings = new HashMap<>();
        for (Addon addon : addonService.getFeaturedAddons()) {
            addonsRatings.put(addon.getId(), ratingService.calculateRating(addon.getId()));
        }
        for (Addon addon : addonService.getNewAddons()) {
            addonsRatings.put(addon.getId(), ratingService.calculateRating(addon.getId()));
        }
        for (Addon addon : addonService.getPopularAddons()) {
            addonsRatings.put(addon.getId(), ratingService.calculateRating(addon.getId()));
        }
        model.addAttribute("addonsRatings", addonsRatings);
        return "index";
    }

    @GetMapping("/not-found")
    public String showNotFoundPage(Model model) {
        return "not-found";
    }

    @GetMapping("/terms-conditions")
    public String showTermsAndConditions() {
        return "terms-conditions";
    }

    @GetMapping("/privacy-policy")
    public String showPrivacyPolicy() {
        return "privacy-policy";
    }

    @PostMapping("")
    public String filter(@ModelAttribute SearchAllDto searchAllDto, Model model) {
        List<Addon> addonList = addonService.search(Optional.of(searchAllDto.getSearchAll()));
        model.addAttribute("addons", addonList);
        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);
        model.addAttribute("ides", ides);
        model.addAttribute("filterAddonDto", new FilterAddonDto());
        return "addons";
    }

    private void insertIdes(Map<String, Object> ides) {
        int id = 1;
        for (IDE ide : ideService.getAll()) {
            ides.put(ide.getName(), id++);
        }
    }
}
