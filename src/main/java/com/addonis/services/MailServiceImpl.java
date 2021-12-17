package com.addonis.services;

import com.addonis.models.ConfirmationToken;
import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;
import com.addonis.models.addon.AddonCode;
import com.addonis.repositories.addon.AddonCodeRepository;
import com.addonis.repositories.confirmationToken.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private static final int VERIFICATION_CODE_LENGTH = 6;

    private final JavaMailSender mailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AddonCodeRepository addonCodeRepository;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, ConfirmationTokenRepository confirmationTokenRepository, AddonCodeRepository addonCodeRepository) {
        this.mailSender = mailSender;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.addonCodeRepository = addonCodeRepository;
    }

    @Override
    public void sendAccountVerificationMail(User recipient) {
        ConfirmationToken confirmationToken = new ConfirmationToken(recipient);
        confirmationTokenRepository.create(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

//        For testing purposes:
//        mailMessage.setTo("gabstoyanova1@gmail.com");

        mailMessage.setTo(recipient.getEmail());
        mailMessage.setText("Please follow the link below to verify your email to secure your account.\n" +
                "http://localhost:8080/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());
        mailMessage.setSubject("Secure Your Addonis Account – Verify Email Address");
        mailSender.send(mailMessage);
    }

    @Override
    public void sendInvitationMail(User inviter, String email) {
        String invitationString = "Hello, dear customer \n" + "" +
                "Your friend %s %s has invited you to join the best Addons Registry web application, Addonis! \n" +
                "Please follow the link below to become a part of us! \n" +
                "http://localhost:8080/auth/register";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        For testing purposes:
//        mailMessage.setTo("tediigeorgieva17@gmail.com");

        mailMessage.setTo(email);
        String firstName = inviter.getFirstName() != null ? inviter.getFirstName() : "";
        String lastName = inviter.getLastName() != null ? inviter.getLastName() : "";
        mailMessage.setText(String.format(invitationString, firstName, lastName));
        mailMessage.setSubject("You're invited to join Addonis!");
        mailSender.send(mailMessage);
    }

    @Override
    public void sendAddonVerificationCode(Addon addonToVerify, User recipient) {
        String code = Utils.getRandomString(VERIFICATION_CODE_LENGTH);
        AddonCode codeEntity = new AddonCode(code, addonToVerify);
        addonCodeRepository.create(codeEntity);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

//        For testing purposes:
//        mailMessage.setTo("gabstoyanova1@gmail.com");

        mailMessage.setTo(recipient.getEmail());
        mailMessage.setSubject("Verify your new add-on. Your code is " + code);
        mailMessage.setText("Hi, friend! Your new add-on has been created! \n" +
                "We would kindly invite you to verify your add-on within 7 days by entering the verification \n" +
                "code you received in the verification page of your addon: http://localhost:8080/addons/verify \n\n" +
                "Your code is: " + code + "" +
                "\nDate of expiration: " + codeEntity.getExpirationDate());
        mailSender.send(mailMessage);
    }
}
