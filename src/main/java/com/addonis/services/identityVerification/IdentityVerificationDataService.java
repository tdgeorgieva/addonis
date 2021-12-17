package com.addonis.services.identityVerification;

import com.addonis.models.IdentityVerificationData;
import com.addonis.models.user.User;

import java.util.List;

public interface IdentityVerificationDataService {
    List<IdentityVerificationData> getAll();

    IdentityVerificationData getById(int id);

    IdentityVerificationData getByUser(User user);

    void create(IdentityVerificationData data);

    void update(IdentityVerificationData data);

    void delete(int id);
}
