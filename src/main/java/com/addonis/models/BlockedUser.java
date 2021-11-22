package com.addonis.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "blocked_users")
public class BlockedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int blockedUserId;

    @Column(name = "blocked_by")
    private int adminId;

    @Column(name = "block_date")
    private LocalDate date;

    public BlockedUser() {
    }

    public BlockedUser(int blockedUserId, int adminId) {
        this.blockedUserId = blockedUserId;
        this.adminId = adminId;
        this.date = LocalDate.now();
    }

    public int getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(int blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
