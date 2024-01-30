package com.example.rayan.confessionapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Confession {
    @Id
    private String id;
    private String confession;
    private String authorEmail;
    private LocalDateTime createdAt;

    public Confession(String confession, String authorEmail, LocalDateTime createdAt) {
        this.confession = confession;
        this.createdAt = createdAt;
        this.authorEmail = authorEmail;
    }
}
