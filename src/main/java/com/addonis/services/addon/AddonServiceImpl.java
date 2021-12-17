package com.addonis.services.addon;

import com.addonis.exceptions.DuplicateEntityException;
import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.exceptions.InvalidCodeException;
import com.addonis.models.user.User;
import com.addonis.models.addon.*;
import com.addonis.repositories.addon.AddonCodeRepository;
import com.addonis.repositories.addon.AddonRepository;
import com.addonis.repositories.review.ReviewRepository;
import com.addonis.services.MailService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.addonis.services.Utils.throwIfNotAdminOrOwner;
import static com.addonis.services.Utils.throwIfUserIsBlocked;


@Service
public class AddonServiceImpl implements AddonService {

    private static final String INVALID_CODE_ERROR = "Wrong code!";

    private final AddonRepository addonRepository;
    private final AddonCodeRepository addonCodeRepository;
    private final ReviewRepository reviewRepository;
    private final MailService mailService;

    public AddonServiceImpl(AddonRepository addonRepository, AddonCodeRepository addonCodeRepository, ReviewRepository reviewRepository, MailService mailService) {
        this.addonRepository = addonRepository;
        this.addonCodeRepository = addonCodeRepository;
        this.reviewRepository = reviewRepository;
        this.mailService = mailService;
    }

    @Override
    public Page<Addon> getAddons(AddonPage addonPage, AddonSearchCriteria addonSearchCriteria, boolean isAdmin) {
        return addonRepository.findAllWithFilters(addonPage, addonSearchCriteria, isAdmin);
    }

    @Override
    public List<Addon> getAll() {
        return addonRepository.getAll();
    }

    @Override
    public List<Addon> search(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        return addonRepository.search(search);
    }

    @Override
    public Addon getById(int id) {
        return addonRepository.getById(id);
    }

    @Override
    public Addon getByName(String name) {
        return addonRepository.getByField("name", name);
    }

    @Override
    public void create(Addon addon, User user) {
        checkDuplicateExists("name", addon.getName());
        addonRepository.create(addon);
        mailService.sendAddonVerificationCode(addon, user);
    }

    @Override
    public void update(Addon addon, User user) {
        throwIfNotAdminOrOwner(user, addon.getUser().getId());
        throwIfUserIsBlocked(user);
        addonRepository.update(addon);
    }

    @Override
    public void delete(int id, User user) {
        throwIfNotAdminOrOwner(user, addonRepository.getById(id).getUser().getId());
        throwIfUserIsBlocked(user);
        reviewRepository.deleteByAddon(id);
        addonRepository.delete(id);
    }

    @Override
    public List<Addon> filter(Optional<String> name, Optional<Integer> ideId, Optional<String> sort) {
        return addonRepository.filter(name, ideId, sort);
    }
    @Override
    public List<Addon> sortAddonsByDownloads() {
        return addonRepository.sortAddonsByDownloads();
    }
    @Override
    public List<Addon> getFeaturedAddons() {
        return addonRepository.getFeaturedAddons();
    }
    @Override
    public List<Addon> getUnapprovedAddons() {
        return addonRepository.getUnapprovedAddons();
    }
    @Override
    public List<Addon> getNewAddons() {
        return addonRepository.getNewAddons();
    }
    @Override
    public List<Addon> getPopularAddons() {
        return addonRepository.getPopularAddons();
    }
    @Override
    public List<Addon> getAddonsByUserId(User user) {
        return addonRepository.getAddonsByUserId(user.getId());
    }

    @Override
    public List<Addon> getDraftsByUserId(int id) {
        return addonRepository.getDraftsByUserId(id);
    }

    @Override
    public List<Addon> sortAddonsByLastCommitDate(List<Addon> list) {
        return addonRepository.sortAddonsByLastCommitDate(list);
    }

    @Override
    public Addon verifyAddon(String verificationCode, User user) {
        try {
            AddonCode code = addonCodeRepository.findByCode(verificationCode);
            Addon addon = addonRepository.getById(code.getAddon().getId());

            if (!addon.getUser().getUsername().equals(user.getUsername())) {
                throw new InvalidCodeException(INVALID_CODE_ERROR);
            }
            addon.setStatus(AddonStatus.VERIFIED);
            addonRepository.update(addon);
            addonCodeRepository.delete(code.getId());
            return addon;
        } catch (EntityNotFoundException e) {
            throw new InvalidCodeException(INVALID_CODE_ERROR);
        }
    }

    @Override
    public void approve(Addon addon, User user) {
        addon.setStatus(AddonStatus.APPROVED);
        update(addon, user);
    }

    @Override
    public void feature(Addon addon, User admin) {
        addon.setFeatured(true);
        update(addon, admin);
    }

    private void checkDuplicateExists(String attribute, String value) {
        boolean duplicateExists = true;
        try {
            addonRepository.getByField(attribute, value);
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Addon", attribute, value);
        }
    }
}
