package com.addonis.controllers.rest;

import com.addonis.controllers.AuthenticationHelper;
import com.addonis.dtos.AddonDto;
import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.modelMappers.AddonModelMapper;
import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;
import com.addonis.services.addon.AddonService;
import com.addonis.services.rating.RatingService;
import com.addonis.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.addonis.controllers.Utils.setGitHubInformation;

@RestController
@RequestMapping("/api/addons")
public class AddonController {

    private final AddonService addonService;
    private final UserService userService;
    private final RatingService ratingService;
    private final AddonModelMapper addonModelMapper;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public AddonController(AddonService addonService, AddonModelMapper addonModelMapper, UserService userService, AuthenticationHelper authenticationHelper, RatingService ratingService) {
        this.addonService = addonService;
        this.addonModelMapper = addonModelMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<Addon> getAll() {
        return setAddonInfo();
    }

    @GetMapping("/{id}")
    public Addon getById(@PathVariable int id) {
        Addon addon = addonService.getById(id);
        setGitHubInformation(addon);
        return addon;
    }

    @PostMapping
    public Addon create(@RequestHeader HttpHeaders headers, @Valid @RequestBody AddonDto addonDto) throws IOException {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Addon addon = addonModelMapper.fromDto(addonDto, false);
            addonService.create(addon, user);
            return addon;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Addon update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody AddonDto addonDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Addon addon = addonModelMapper.fromDto(addonDto, id, false);
            addonService.update(addon, user);
            return addon;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            addonService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<Addon> filter(@RequestParam(required = false) Optional<String> name,
                              @RequestParam(required = false) Optional<Integer> ideId,
                              @RequestParam(required = false) Optional<String> sort) {
        boolean isEqual = sort.isPresent() && sort.get().equals("lastCommitDate");
        if (isEqual) {
            List<Addon> list = setAddonInfo();
            return addonService.sortAddonsByLastCommitDate(list);
        }
        return addonService.filter(name, ideId, sort);
    }

    @GetMapping("/users/{id}")
    public List<Addon> getAddonsByUserId(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = userService.getById(id);
            return addonService.getAddonsByUserId(user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/list/popular")
    public List<Addon> listPopular() {
        return addonService.sortAddonsByDownloads();
    }

    @GetMapping("/list/featured")
    public List<Addon> listFeatured() {
        return addonService.getFeaturedAddons();
    }

    @GetMapping("/list/newest")
    public List<Addon> listNewest() {
        return addonService.getNewAddons();
    }

    private List<Addon> setAddonInfo() {
        List<Addon> list = addonService.getAll();
        for (Addon addon : list) {
            setGitHubInformation(addon);
        }
        return list;
    }
}
