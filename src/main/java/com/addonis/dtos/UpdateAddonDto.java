package com.addonis.dtos;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateAddonDto {

    @NotNull
    @NotEmpty
    private String description;

//    @Lob
//    private byte[] binaryContent;

    private int ideId;

    public void setTags(String tags) {
        this.tags = tags;
    }

    private String tags;

    public String getTags() {
        return tags;
    }
//    @Lob
//    private byte[] image;

    private String link;

//    private AddonType type;

    public UpdateAddonDto() {
    }

    public UpdateAddonDto(String name, String description,
                          byte[] binaryContent, int ideId,
                          String link) {
        this.description = description;
       // this.binaryContent = binaryContent;
        this.ideId = ideId;
//        this.image = image;
        this.link = link;
//        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public byte[] getBinaryContent() {
//        return binaryContent;
//    }
//
//    public void setBinaryContent(byte[] binaryContent) {
//        this.binaryContent = binaryContent;
//    }

    public int getIdeId() {
        return ideId;
    }

    public void setIdeId(int ideId) {
        this.ideId = ideId;
    }

    public String getLink() { return link; }

    public void setLink(String link) {
        this.link = link;
    }
//
//    public AddonType getType() {
//        return type;
//    }
//
//    public void setType(AddonType type) {
//        this.type = type;
//    }

}
