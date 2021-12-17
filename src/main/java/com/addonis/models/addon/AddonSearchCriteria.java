package com.addonis.models.addon;

public class AddonSearchCriteria {
    private int ideId = -1;
    private String addonName;

    public int getIdeId() {
        return ideId;
    }

    public void setIdeId(int ideName) {
        this.ideId = ideName;
    }

    public String getAddonName() {
        return addonName;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }
}
