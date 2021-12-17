package com.addonis.models.addon;

import com.addonis.models.IDE;
import com.addonis.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "addons")
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addon_id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private AddonStatus status = AddonStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "ide_id")
    private IDE ide;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "downloads_count")
    private int downloadsCount;

    @Column(name = "link")
    private String link;

    @Column(name = "upload_date")
    private LocalDate date;

    @Column(name = "type")
    private AddonType type = AddonType.REGULAR;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "addon_tags",
            joinColumns = {@JoinColumn(name = "addon_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<AddonTag> tags;

    @Column(name = "is_featured")
    private boolean isFeatured;

    @Transient
    private int issues;

    @Transient
    private int pulls;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastCommitDate;

    @Transient
    private String lastCommitTitle;

    public Addon() {
    }

    public Addon(int id, String name, User user, String description,
                 IDE ide, byte[] image,
                 String link, LocalDate date, AddonType type) {

        this.id = id;
        this.name = name;
        this.user = user;
        this.description = description;
        this.ide = ide;
        this.image = image;
        this.link = link;
        this.date = date;
        this.type = type;
        this.isFeatured = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddonStatus getStatus() {
        return status;
    }

    public void setStatus(AddonStatus status) {
        this.status = status;
    }

    public IDE getIde() {
        return ide;
    }

    public void setIde(IDE ide) {
        this.ide = ide;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(int downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AddonType getType() {
        return type;
    }

    public void setType(AddonType type) {
        this.type = type;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public int getIssues() {
        return issues;
    }

    public void setIssues(int issues) {
        this.issues = issues;
    }

    public int getPulls() { return pulls; }

    public void setPulls(int pulls) {
        this.pulls = pulls;
    }

    public LocalDate getLastCommitDate() {
        return lastCommitDate;
    }

    public void setLastCommitDate(LocalDate lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public String getLastCommitTitle() {
        return lastCommitTitle;
    }

    public void setLastCommitTitle(String lastCommitTitle) {
        this.lastCommitTitle = lastCommitTitle;
    }

    public boolean isApproved() {
        return this.status.equals(AddonStatus.APPROVED);
    }

    public Set<AddonTag> getTags() {
        return tags;
    }

    public void setTags(Set<AddonTag> tags) {
        this.tags = tags;
    }

    public String getBase6EncodedImage() {
        return Base64.encodeBase64String(this.getImage());
    }
}
