package com.blogcraft.model;

import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String comment;

    // Constructors
    public Comment() {}
    public Comment(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

