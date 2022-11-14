package com.nhnacademy.springmvc.controller;

import java.time.LocalDateTime;

public class Current {

    private LocalDateTime now;

    public Current(LocalDateTime now) {
        this.now = now;
    }

    public LocalDateTime getNow() {
        System.out.println("Call Current.getNow()");
        return now;
    }
}
