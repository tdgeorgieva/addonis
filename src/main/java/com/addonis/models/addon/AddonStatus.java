package com.addonis.models.addon;

public enum AddonStatus {

    DRAFT, PENDING, VERIFIED, APPROVED;

    @Override
    public String toString() {
        switch(this) {
            case DRAFT:
                return "Draft";
            case PENDING:
                return "Pending";
            case VERIFIED:
                return "Verified";
            case APPROVED:
                return "Approved";
            default:
                return "";
        }
    }

}
