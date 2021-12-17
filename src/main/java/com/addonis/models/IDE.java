package com.addonis.models;

import javax.persistence.*;

@Entity
@Table(name = "ides")
public class IDE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ide_id")
    private int id;

    @Column(name = "name")
    private String name;

    public IDE() {
    }

    public IDE(int id, String name) {
        this.id = id;
        this.name = name;
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
}
