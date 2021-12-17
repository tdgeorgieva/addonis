package com.addonis.models.addon;

public enum AddonType {
    REGULAR, JOINT;

    @Override
    public String toString() {
        switch(this) {
            case REGULAR:
                return "Regular";
            case JOINT:
                return "Joint";
            default:
                return "";
        }
    }
}
