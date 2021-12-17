package com.addonis.controllers.mvc;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.*;
import com.addonis.exceptions.*;
import com.addonis.modelMappers.AddonModelMapper;
import com.addonis.modelMappers.RatingModelMapper;
import com.addonis.modelMappers.ReviewModelMapper;
import com.addonis.models.IDE;
import com.addonis.models.Review;
import com.addonis.models.user.User;
import com.addonis.models.addon.*;
import com.addonis.repositories.addon.TagRepository;
import com.addonis.services.IDE.IDEService;
import com.addonis.services.addon.AddonService;
import com.addonis.services.rating.RatingService;
import com.addonis.services.review.ReviewService;
import com.addonis.services.user.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.addonis.controllers.Utils.setGitHubInformation;

@Controller
@RequestMapping("/addons")
@EnableScheduling
public class AddonMvcController extends BaseMvcController {

    private final AddonModelMapper addonModelMapper;
    private final ReviewModelMapper reviewModelMapper;
    private final RatingModelMapper ratingModelMapper;
    private final AddonService addonService;
    private final IDEService ideService;
    private final ReviewService reviewService;
    private final TagRepository tagRepository;
    private final AuthenticationHelper authenticationHelper;
    private final RatingService ratingService;


    @Autowired
    public AddonMvcController(UserService userService, AddonModelMapper addonModelMapper, ReviewModelMapper reviewModelMapper,
                              RatingModelMapper ratingModelMapper, AddonService addonService, IDEService ideService,
                              ReviewService reviewService, TagRepository tagRepository,
                              AuthenticationHelper authenticationHelper, RatingService ratingService) {
        super(userService);
        this.addonModelMapper = addonModelMapper;
        this.reviewModelMapper = reviewModelMapper;
        this.ratingModelMapper = ratingModelMapper;
        this.addonService = addonService;
        this.ideService = ideService;
        this.reviewService = reviewService;
        this.tagRepository = tagRepository;
        this.authenticationHelper = authenticationHelper;
        this.ratingService = ratingService;
    }

    @GetMapping
    public String showAllAddons(HttpSession session, AddonPage page, AddonSearchCriteria addonSearchCriteria, Model model) {

        Page<Addon> resp;
        try {
            User user = authenticationHelper.tryGetUser(session);

            resp = addonService.getAddons(page, addonSearchCriteria, user.isAdmin());

        } catch (AuthenticationFailureException authException) {

            resp = addonService.getAddons(page, addonSearchCriteria, false);

        }
        model.addAttribute("heading", "All add-ons");
        model.addAttribute("addons", resp.stream().collect(Collectors.toList()));

        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);

        model.addAttribute("ides", ides);
        model.addAttribute("filterAddonDto", new FilterAddonDto(addonSearchCriteria.getIdeId(), addonSearchCriteria.getAddonName(), page.getSortBy()));
        model.addAttribute("page", page);
        model.addAttribute("totalPages", resp.getTotalPages());
        List<String> queryParams = new ArrayList<>();
        if (page.getPageSize() != 0) {
            queryParams.add("pageSize=" + page.getPageSize());
        }
        if (addonSearchCriteria.getAddonName() != null && addonSearchCriteria.getAddonName().equals("")) {
            queryParams.add("addonName=" + addonSearchCriteria.getAddonName());
        }

        queryParams.add("ideId=" + addonSearchCriteria.getIdeId());

        if (page.getSortBy() != null && !page.getSortBy().equals("")) {
            queryParams.add("sortBy=" + page.getSortBy());
        }
        model.addAttribute("queryPath", String.join("&", queryParams));
        return "addons";
    }

    @GetMapping("/{id}")
    public String showAddon(HttpSession session, @PathVariable int id, Model model) {
        boolean isLogged = false;

        try {
            User user = authenticationHelper.tryGetUser(session);
            isLogged = true;
            model.addAttribute("currentUser", user);
            model.addAttribute("user", user.getUserFullName());

            model.addAttribute("ReviewDto", new ReviewDto());

        } catch (AuthenticationFailureException authException) {

        }
        Addon addon = addonService.getById(id);
        model.addAttribute("reviewsCount", reviewService.getByAddon(id).size());
        model.addAttribute("reviews", reviewService.getByAddon(id));
        model.addAttribute("isLogged", isLogged);
        model.addAttribute("tags", addon.getTags());
        model.addAttribute("addons", addonService.getAll());
        model.addAttribute("popularAddons", addonService.filter(Optional.empty(), Optional.empty(), Optional.of("downloads_descending")));
        model.addAttribute("newAddons", addonService.filter(Optional.empty(), Optional.empty(), Optional.of("upload_descending")));

        if (addon.getImage() != null) {
            String base64EncodedImage = Base64.encodeBase64String(addon.getImage());
            model.addAttribute("encodedPhoto", base64EncodedImage);
        }
        setGitHubInformation(addon);
        double rating = ratingService.calculateRating(addon.getId());
        model.addAttribute("rating", rating);
        model.addAttribute("addon", addon);

        HashMap<Integer, Double> addonsRatings = new HashMap<>();
        for (Addon a : addonService.getNewAddons()) {
            addonsRatings.put(a.getId(), ratingService.calculateRating(a.getId()));
        }
        model.addAttribute("addonsRatings", addonsRatings);

        return "addon-info";
    }

    @PatchMapping(path = "/{id}/rating")
    public @ResponseBody
    double rateAddon(HttpSession session,
                     @PathVariable int id, @RequestBody RatingDto ratingDto) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Addon addon = addonService.getById(id);
            int r = ratingDto.getRating();
            ratingDto.setAddonId(addon.getId());
            ratingDto.setUserId(user.getId());
            ratingDto.setRating(r);
            Rating rating = ratingModelMapper.fromDto(ratingDto);
            ratingService.create(rating);
            return ratingService.calculateRating(addon.getId());
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/pending")
    public String showPendingAddons(Model model) {
        model.addAttribute("heading", "Pending addons");

        model.addAttribute("addons", addonService.getUnapprovedAddons());
        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);
        model.addAttribute("ides", ides);

        return "pending-addons";
    }


    @GetMapping("/new")
    public String showCreateAddonPage(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("currentUser", user);
        model.addAttribute("addonDto", new AddonDto());
        model.addAttribute("addonType", AddonType.values());

        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);
        model.addAttribute("ides", ides);

        return "addon-new";
    }

    @PostMapping("/new")
    public String createAddon(@RequestParam(required = false, value = "draft") String draft,
                              @Valid @ModelAttribute("addonDto") AddonDto addonDto,
                              Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        Addon addon;

        try {
            // if it is a draft
            if (draft != null) {
                addon = addonModelMapper.fromDto(addonDto, true);
                addon.setUser(user);
                addonService.create(addon, user);
                if (addonDto.getBinaryContent() != null) {
                    Path path = Paths.get("src/main/resources/static/assets/addons//" + addon.getId());
                    Files.write(path, addonDto.getBinaryContent().getBytes());
                }
                return showAddon(session, addon.getId(), model);
            }
            addon = addonModelMapper.fromDto(addonDto, false);
            addon.setUser(user);

            addonService.create(addon, user);
            if (addonDto.getBinaryContent() != null) {
                Path path = Paths.get("src/main/resources/static/assets/addons//" + addon.getId());
                Files.write(path, addonDto.getBinaryContent().getBytes());
            }
            return "successful-addon-creation";
        } catch (DuplicateEntityException e) {
            model.addAttribute("exceptionMessage", "An add-on with this name already exists!");
            return showCreateAddonPage(model, session);
        } catch (InvalidInputException | IOException ioException) {
            model.addAttribute("exceptionMessage", "Please provide a valid input to all fields to create a new add-on!");
            return showCreateAddonPage(model, session);
        }
    }

    @PostMapping("/{id}/addReview")
    public String createReview(@Valid @ModelAttribute("reviewDto") ReviewDto reviewDto,
                               BindingResult errors,
                               @PathVariable int id,
                               Model model,
                               HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            return "redirect:/addons/{id}";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            reviewDto.setDate(LocalDate.now());
            reviewDto.setAddonId(id);
            reviewDto.setUserId(user.getId());
            Review review = reviewModelMapper.fromDto(reviewDto);
            reviewService.create(review);
            return "redirect:/addons/{id}";
        } catch (Exception e) {
            return "redirect:/addons/{id}";
        }
    }

    @PostMapping("")
    public String filter(@ModelAttribute FilterAddonDto filterAddonDto, Model model, HttpSession session) {
        try {
            model.addAttribute("heading", "Search results");
            Map<String, Object> ides = new HashMap<>();
            insertIdes(ides);
            model.addAttribute("ides", ides);

            List<Addon> addonsList = addonService.filter(Optional.of(filterAddonDto.getName()), Optional.of(filterAddonDto.getIdeId()), Optional.of(filterAddonDto.getSort()));

            model.addAttribute("addons", addonsList);

            return "addons";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    private void insertIdes(Map<String, Object> ides) {
        int id = 1;
        for (IDE ide : ideService.getAll()) {
            ides.put(ide.getName(), id++);
        }
    }

    @GetMapping("/verify")
    public String verifyAddonPage(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("addonCodeDto", new AddonCodeDto());
        return "verify-addon";
    }

    @PostMapping("/verify")
    public String verifyAddon(@Valid @ModelAttribute("addonCodeDto") AddonCodeDto codeDto,
                              BindingResult errors,
                              Model model,
                              HttpSession session) {
        if (errors.hasErrors()) {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "verify-addon";
        }
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            Addon verified = addonService.verifyAddon(codeDto.getCode(), user);

            return "confirm-addon";
        } catch (InvalidCodeException e) {
            return "redirect:/addons/verify";
        }
    }


    @GetMapping("/filtered/featured")
    public String showFeaturedAddons(Model model) {
        model.addAttribute("heading", "Featured add-ons");

        model.addAttribute("addons", addonService.getFeaturedAddons());

        return "featured-addons";
    }

    @GetMapping("/{id}/approve")
    public String approveAddonPage(@PathVariable int id, Model model, HttpSession session) {
        User admin = authenticationHelper.tryGetUser(session);
        Addon addon = addonService.getById(id);
//        add tags
        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);
        Set<Integer> addonTags = addon.getTags().stream()
                .map(AddonTag::getId)
                .collect(Collectors.toSet());
        model.addAttribute("addonTags", addonTags);
        model.addAttribute("tags", tagRepository.getAll());
        model.addAttribute("tagsDto", new TagsDto());
        model.addAttribute("addon", addon);

        return "approve-addon";
    }

    @PostMapping("/{id}/approve")
    public String approveAddon(@Valid @ModelAttribute("tagsDto") TagsDto tagsDto,
                               @PathVariable int id, Model model, HttpSession session) {
        User admin = authenticationHelper.tryGetUser(session);
        Addon addon = addonService.getById(id);

        Set<AddonTag> tags = getAddonTags(tagsDto);
        addon.setTags(tags);
        addonService.approve(addon, admin);
        return "redirect:/addons/{id}";
    }

    @GetMapping("/{id}/markFeatured")
    public String featureAddon(@PathVariable int id, Model model, HttpSession session) {
        User admin = authenticationHelper.tryGetUser(session);
        Addon addon = addonService.getById(id);

        addonService.feature(addon, admin);
        return "redirect:/addons";
    }

    @GetMapping("/{id}/delete")
    public String deleteAddon(@PathVariable int id, Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        addonService.delete(id, user);

        return "redirect:/addons";
    }

    @GetMapping("/{id}/update")
    public String showUpdateAddonPage(@PathVariable int id, Model model, HttpSession session) throws IOException {
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("currentUser", user);

        Addon addon = addonService.getById(id);
        Map<String, Object> ides = new HashMap<>();
        insertIdes(ides);
        Set<Integer> addonTags = addon.getTags().stream()
                .map(AddonTag::getId)
                .collect(Collectors.toSet());

        AddonDto addonDto = addonModelMapper.toDto(addon);
        model.addAttribute("addonTags", addonTags);
        model.addAttribute("addonDto", addonDto);
        model.addAttribute("addon", addon);
        model.addAttribute("tags", tagRepository.getAll());
        model.addAttribute("ides", ides);

        return "addon-update";
    }

    @PostMapping("/{id}/update")
    public String updateAddon(@RequestParam(required = false, value = "draft") String draft,
                              @Valid @ModelAttribute("addonDto") AddonDto addonDto,
                              @PathVariable int id,
                              Model model, HttpSession session) throws IOException {
        User user = authenticationHelper.tryGetUser(session);
        Addon addon;
        try {
            if (draft != null) {
                addon = addonModelMapper.fromDto(addonDto, id, true);
            } else {
                addon = addonModelMapper.fromDto(addonDto, id, false);
            }
            addonService.update(addon, user);
            return "redirect:/addons/{id}";
        } catch (DuplicateEntityException e) {
            model.addAttribute("exceptionMessage", "An add-on with this name already exists!");
            return showUpdateAddonPage(id, model, session);
        } catch (InvalidInputException e) {
            model.addAttribute("exceptionMessage", "Please provide a valid input to all fields to create a new add-on!");
            return showUpdateAddonPage(id, model, session);
        }
    }


    @GetMapping("/{id}/download")
    @ResponseBody
    public void downloadFile(@PathVariable int id, HttpServletResponse response) {

        Addon addon = addonService.getById(id);

        Path file = Paths.get("src/main/resources/static/assets/addons//" + addon.getId());
        if (Files.exists(file)) {
            response.setContentType("application/png");
            response.addHeader("Content-Disposition", "attachment; filename=" + addon.getId());
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
                addon.setDownloadsCount(addon.getDownloadsCount() + 1);
                addonService.update(addon, addon.getUser());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Set<AddonTag> getAddonTags(TagsDto tagsDto) {
        List<AddonTag> list = tagRepository.getAll();
        Map<Integer, String> map = new HashMap<>();
        for (AddonTag tag : list) {
            map.put(tag.getId(), tag.getName());
        }
        Set<AddonTag> tags = new HashSet<>();
        if (tagsDto.getTags() != null) {
            for (String tagIdStr : tagsDto.getTags()) {
                int tagID = Integer.parseInt(tagIdStr);
                if (map.containsKey(tagID)) {
                    String tagName = map.get(tagID);
                    tags.add(new AddonTag(tagID, tagName));
                }
            }
        }
        return tags;
    }

}


