package com.addonis.dtos;

import com.addonis.models.addon.AddonStatus;
import com.addonis.models.addon.AddonTag;
import com.addonis.models.addon.AddonType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class AddonDto {

//    @Valid
//    @Size(min = 3, max = 10, message = "Addon name is not valid! Size should be between 3 and 30 symbols!")
    private String name;

//    @NotNull
    private int userId;

//    @NotNull
    private String description;

    @Lob
//    @NotNull
    private MultipartFile binaryContent;

    private AddonStatus status;

//    @NotNull
    private int ideId;

    MultipartFile file;

    private String downloadLink;

//    @NotEmpty(message = "Addon repository link must not be empty!")
    private String link;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uploadDate;

    private AddonType type;

    private String[] tags;

    public AddonDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(MultipartFile binaryContent) {
        this.binaryContent = binaryContent;
    }

    public AddonStatus getStatus() {
        return status;
    }

    public void setStatus(AddonStatus status) {
        this.status = status;
    }

    public int getIdeId() {
        return ideId;
    }

    public void setIdeId(int ideId) {
        this.ideId = ideId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getLink() { return link; }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public AddonType getType() {
        return type;
    }

    public void setType(AddonType type) {
        this.type = type;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
