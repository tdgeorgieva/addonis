package com.addonis.services;

import com.addonis.exceptions.UnauthorizedOperationException;
import com.addonis.models.user.User;

public class Utils {

    private static final String ADMIN_ROLE = "admin";
    private static final String ADMIN_ERROR = "Only admins can block other users!";
    private static final String NOT_AUTHORIZED_ERROR = "You are not authorized to perform the requested action!";

    public static void throwIfNotAdmin(User admin) {
        if (!admin.getRole().getName().equalsIgnoreCase(ADMIN_ROLE)) {
            throw new UnauthorizedOperationException(ADMIN_ERROR);
        }
    }

    public static void throwIfNotAdminOrOwner(User user, int ownerId) {
        if (!user.isAdmin() && user.getId() != ownerId) {
            throw new UnauthorizedOperationException(NOT_AUTHORIZED_ERROR);
        }
    }

    public static void throwIfUserIsBlocked(User user) {
        if (user.isBlocked()) {
            throw new UnauthorizedOperationException(NOT_AUTHORIZED_ERROR);
        }
    }

    public static String getRandomString(int length) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
