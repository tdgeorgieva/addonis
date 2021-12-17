package com.addonis.repositories.addon;

import com.addonis.models.addon.AddonCode;
import com.addonis.repositories.BaseCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonCodeRepository extends BaseCRUDRepository<AddonCode> {

    AddonCode findByCode(String code);

    void deleteAutomatically();

}
