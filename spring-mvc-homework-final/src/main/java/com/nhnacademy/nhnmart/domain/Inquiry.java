package com.nhnacademy.nhnmart.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Inquiry {

    @Setter
    private long id;
    private final String title;
    private final String category;
    private final String content;
    private final LocalDateTime createdAt;
    private final String author;
    private final List<String> imageFiles;

    @Setter
    private Answer answer;

    public static Inquiry create(String title, String category, String content, String author) {
        return new Inquiry(title, category, content, author);
    }

    private Inquiry(String title, String category, String content, String author) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.author = author;
        this.imageFiles = new ArrayList<>();
    }
}
