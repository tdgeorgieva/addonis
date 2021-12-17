package com.addonis.models.user;

public enum UserStatus {
    NOT_CONFIRMED, CONFIRMED, VERIFIED, BLOCKED;

    @Override
    public String toString() {
        switch (this) {
            case NOT_CONFIRMED:
                return "Not confirmed";
            case CONFIRMED:
                return "Confirmed";
            case VERIFIED:
                return "Verified";
            case BLOCKED:
                return "Blocked";
            default:
                return "";
        }
    }
}
