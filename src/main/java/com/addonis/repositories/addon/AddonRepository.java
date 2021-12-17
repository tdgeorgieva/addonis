package com.addonis.repositories.addon;

import com.addonis.models.addon.Addon;
import com.addonis.models.addon.AddonPage;
import com.addonis.models.addon.AddonSearchCriteria;
import com.addonis.repositories.BaseCRUDRepository;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

public interface AddonRepository extends BaseCRUDRepository<Addon> {

    List<Addon> search(Optional<String> search);

    List<Addon> filter(Optional<String> name, Optional<Integer> ideId, Optional<String> sort);

    Page<Addon> findAllWithFilters(AddonPage addonPage, AddonSearchCriteria addonSearchCriteria, boolean isAdmin);

    List<Addon> sortAddonsByDownloads();

    List<Addon> getFeaturedAddons();

    List<Addon> getUnapprovedAddons();

    List<Addon> getNewAddons();

    List<Addon> getPopularAddons();

    List<Addon> getAddonsByUserId(int userId);

    List<Addon> getDraftsByUserId(int id);

    List<Addon> sortAddonsByLastCommitDate(List<Addon> list);

    void updateAddon(Addon addon);
}
