package com.addonis.models.registration;

import com.addonis.dtos.Matchable;
import com.addonis.dtos.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Matchable matchablePassword = (Matchable) obj;
        return matchablePassword.getPassword().equals(matchablePassword.getMatchingPassword());
    }
}