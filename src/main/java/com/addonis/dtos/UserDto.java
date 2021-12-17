package com.addonis.dtos;

import com.addonis.models.registration.ValidEmail;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.CallableStatement;

public class UserDto {

    @ValidEmail
    private String email;

    @Valid
    @Size(min = 10, max = 10, message = "Phone number is not valid! Size should be 10 characters!")
    private String phoneNumber;

    private String firstName;

    private String lastName;

//    @NotNull(message = "The photo file must be provided")
    MultipartFile file;

    public UserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
