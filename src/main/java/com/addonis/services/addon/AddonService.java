package com.addonis.services.addon;

import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;
import com.addonis.models.addon.AddonPage;
import com.addonis.models.addon.AddonSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AddonService {

    List<Addon> getAll();

    List<Addon> search(Optional<String> search);

    Addon getById(int id);

    Page<Addon> getAddons(AddonPage addonPage, AddonSearchCriteria addonSearchCriteria, boolean isAdmin);

    Addon getByName(String name);

    void create(Addon addon, User user);

    void update(Addon addon, User user);

    void delete(int id, User user);

    List<Addon> filter(Optional<String> name, Optional<Integer> ideId, Optional<String> sort);

    List<Addon> sortAddonsByDownloads();

    List<Addon> getFeaturedAddons();

    List<Addon> getUnapprovedAddons();

    List<Addon> getNewAddons();

    List<Addon> getPopularAddons();

    List<Addon> getAddonsByUserId(User user);

    List<Addon> getDraftsByUserId(int id);

    List<Addon> sortAddonsByLastCommitDate(List<Addon> list);

    Addon verifyAddon(String verificationCode, User user);

    void approve(Addon addon, User user);

    void feature(Addon addon, User admin);
}
