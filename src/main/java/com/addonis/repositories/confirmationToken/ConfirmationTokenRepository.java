package com.addonis.repositories.confirmationToken;

import com.addonis.models.ConfirmationToken;
import com.addonis.repositories.BaseCRUDRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends BaseCRUDRepository<ConfirmationToken> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
