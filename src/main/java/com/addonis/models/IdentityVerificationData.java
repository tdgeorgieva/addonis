package com.addonis.models;

import com.addonis.models.user.User;

import javax.persistence.*;

@Entity
@Table(name = "users_verification")
public class IdentityVerificationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "id_card_photo")
    private byte[] idCardPhoto;

    @Lob
    @Column(name = "selfie_photo")
    private byte[] selfiePhoto;

    public IdentityVerificationData() {
    }

    public IdentityVerificationData(int id, User user, byte[] idCardPhoto, byte[] selfiePhoto) {
        this.id = id;
        this.user = user;
        this.idCardPhoto = idCardPhoto;
        this.selfiePhoto = selfiePhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getIdCardPhoto() {
        return idCardPhoto;
    }

    public void setIdCardPhoto(byte[] idCardPhoto) {
        this.idCardPhoto = idCardPhoto;
    }

    public byte[] getSelfiePhoto() {
        return selfiePhoto;
    }

    public void setSelfiePhoto(byte[] selfiePhoto) {
        this.selfiePhoto = selfiePhoto;
    }
}
