package com.addonis.models;

import javax.persistence.*;

@Entity
@Table(name = "addons")
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addon_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image", columnDefinition="BLOB")
    private byte[] image;

    @Lob
    @Column(name = "binary_content", columnDefinition="BLOB")
    private byte[] file;

    @Column(name = "downloads_count")
    private float downloadsCount;

    @Column(name = "download_link")
    private float downloadLink;
}
