package com.nhnacademy.nhnmart.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Answer {

    private final String comment;
    private final LocalDateTime createdAt;
    private final String adminName;

    public static Answer create(String comment, String adminName) {
        return new Answer(comment, adminName);
    }

    private Answer(String comment, String adminName) {
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.adminName = adminName;
    }
}
