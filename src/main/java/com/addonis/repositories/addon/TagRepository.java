package com.addonis.repositories.addon;

import com.addonis.models.addon.AddonTag;

import java.util.List;

public interface TagRepository {

    List<AddonTag> getAll();

    AddonTag getById(int id);

    void create(AddonTag tag);

    void delete(int id);

}
