package com.addonis.models.user;

import com.addonis.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "users")
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "status")
    private UserStatus status;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "follow_connections",
            joinColumns = {@JoinColumn(name = "followed_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "follower_user_id")}
    )
    private Set<User> followers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "follow_connections",
            joinColumns = {@JoinColumn(name = "follower_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "followed_user_id")}
    )
    private Set<User> following;

    public User() {
    }

    public User(int id, String username, String password, String email, String phoneNumber,
                Role role, String firstName, String lastName, byte[] photo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.status = UserStatus.NOT_CONFIRMED;
        this.following = new TreeSet<>();
        this.followers = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) throws IOException {
        this.photo = photo;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public void getFollowedBy(User user) {
        this.followers.add(user);
    }

    public void follow(User user) {
        this.following.add(user);
    }

    public int getNumberOfFollowers() {
        return this.followers.size();
    }

    public int getNumberOfFollowing() {
        return this.following.size();
    }

    public boolean isVerified() {
        return this.status == UserStatus.VERIFIED;
    }

    public String getUserFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public boolean isBlocked() {
        return this.status == UserStatus.BLOCKED;
    }

    public boolean isAdmin() {
        return this.role.getName().equalsIgnoreCase("admin");
    }

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.username);
    }

    public void unfollow(User followed) {
        this.following.remove(followed);
    }

    public void getUnfollowedBy(User follower) {
        this.followers.remove(follower);
    }

    public boolean followersContains(User user) {
        return this.followers.stream().anyMatch(f -> f.getUsername().equals(user.getUsername()));
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            User user = (User) object;
            if (this.username.equals(user.getUsername())) {
                result = true;
            }
        }
        return result;
    }

    public String getEncodedImage() {
        String base64EncodedImage;
        if (this.getPhoto() != null) {
            base64EncodedImage = Base64.encodeBase64String(this.getPhoto());
        } else {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File("images/users/defaultProfilePhoto.png"));
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", output);
                byte[] data = output.toByteArray();
                base64EncodedImage = Base64.encodeBase64String(data);
            } catch (IOException e) {
                return null;
            }
        }
        return base64EncodedImage;
    }
}
