package com.addonis.dtos;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateAddonDto {

    private String name;

    private String description;

    @Lob
    private byte[] binaryContent;

    private int ideId;

//    @Lob
//    private byte[] image;

    private String link;

//    private AddonType type;

    public CreateAddonDto() {
    }

    public CreateAddonDto(String name, String description,
                    byte[] binaryContent, int ideId,
                    String link) {
        this.name = name;
        this.description = description;
        this.binaryContent = binaryContent;
        this.ideId = ideId;
//        this.image = image;
        this.link = link;
//        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }

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
