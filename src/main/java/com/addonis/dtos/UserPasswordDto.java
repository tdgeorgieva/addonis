package com.addonis.dtos;

import com.addonis.models.registration.PasswordMatches;
import com.addonis.models.registration.ValidPassword;

import javax.validation.constraints.NotEmpty;

@PasswordMatches
public class UserPasswordDto implements Matchable {

    @NotEmpty(message = "This field is required!")
    private String oldPassword;

    @ValidPassword
    @NotEmpty(message = "This field is required!")
    private String password;

    @NotEmpty(message = "This field is required!")
    private String matchingPassword;

    public UserPasswordDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
