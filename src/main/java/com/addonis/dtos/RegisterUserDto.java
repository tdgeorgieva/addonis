package com.addonis.dtos;

import com.addonis.models.registration.PasswordMatches;
import com.addonis.models.registration.ValidEmail;
import com.addonis.models.registration.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class RegisterUserDto implements Matchable {

    private String firstName;

    private String lastName;

    @NotNull
    @NotEmpty(message = "This field is required!")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters long!")
    private String username;

    @NotNull
    @NotEmpty(message = "This field is required!")
    @ValidPassword
    private String password;
    private String matchingPassword;

    @NotEmpty(message = "This field is required!")
    @Size(min = 10, max = 10, message = "Phone number is not valid!")
    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "This field is required!")
    @ValidEmail
    private String email;

    public RegisterUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}