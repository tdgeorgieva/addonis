package com.addonis.modelMappers;

import com.addonis.dtos.IdentityVerificationDto;
import com.addonis.models.IdentityVerificationData;
import com.addonis.repositories.identityVerification.IdentityVerificationDataRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IdentityVerificationMapper {

    private final IdentityVerificationDataRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public IdentityVerificationMapper(IdentityVerificationDataRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public IdentityVerificationData fromDto(IdentityVerificationDto dto) throws IOException {
        IdentityVerificationData data = new IdentityVerificationData();
        dtoToObject(dto, data);
        return data;
    }

    private void dtoToObject(IdentityVerificationDto dto, IdentityVerificationData data) throws IOException {
        data.setIdCardPhoto(dto.getIdCardPhoto().getBytes());
        data.setSelfiePhoto(dto.getSelfiePhoto().getBytes());
    }

}
