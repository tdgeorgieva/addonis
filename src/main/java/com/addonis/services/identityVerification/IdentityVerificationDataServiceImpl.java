package com.addonis.services.identityVerification;

import com.addonis.models.IdentityVerificationData;
import com.addonis.models.user.User;
import com.addonis.repositories.identityVerification.IdentityVerificationDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityVerificationDataServiceImpl implements IdentityVerificationDataService {

    private final IdentityVerificationDataRepository repository;

    @Autowired
    public IdentityVerificationDataServiceImpl(IdentityVerificationDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<IdentityVerificationData> getAll() {
        return repository.getAll();
    }

    @Override
    public IdentityVerificationData getById(int id) {
        return repository.getById(id);
    }

    @Override
    public IdentityVerificationData getByUser(User user) {
        return repository.getByField("user.id", user.getId());
    }

    @Override
    public void create(IdentityVerificationData data) {
        repository.create(data);
    }

    @Override
    public void update(IdentityVerificationData data) {
        repository.update(data);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
