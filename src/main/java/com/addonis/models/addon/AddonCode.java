package com.addonis.models.addon;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "addon_codes")
public class AddonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name="addon_id")
    private Addon addon;

    public AddonCode() {}

    public AddonCode(String code, Addon addon) {
        this.code = code;
        this.expirationDate = LocalDate.now().plusDays(7);
        this.addon = addon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }
}
