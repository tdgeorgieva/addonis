package com.addonis.dtos;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class IdentityVerificationDto {

    @NotNull
    private int userId;

    @NotNull(message = "The photo file must be provided")
    MultipartFile idCardPhoto;

    @NotNull(message = "The photo file must be provided")
    MultipartFile selfiePhoto;

    public IdentityVerificationDto() {
    }

    public IdentityVerificationDto(int userId, MultipartFile idCardPhoto, MultipartFile selfiePhoto) {
        this.userId = userId;
        this.idCardPhoto = idCardPhoto;
        this.selfiePhoto = selfiePhoto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MultipartFile getIdCardPhoto() {
        return idCardPhoto;
    }

    public void setIdCardPhoto(MultipartFile idCardPhoto) {
        this.idCardPhoto = idCardPhoto;
    }

    public MultipartFile getSelfiePhoto() {
        return selfiePhoto;
    }

    public void setSelfiePhoto(MultipartFile selfiePhoto) {
        this.selfiePhoto = selfiePhoto;
    }
}
