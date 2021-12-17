package com.addonis.services;

import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;

public interface MailService {

    void sendAccountVerificationMail(User recipient);

    void sendInvitationMail(User inviter, String email);

    void sendAddonVerificationCode(Addon addonToVerify, User recipient);

}
