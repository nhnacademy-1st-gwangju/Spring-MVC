package com.nhnacademy.nhnmart.domain;

import lombok.Getter;

@Getter
public class User {

    private final String id;
    private final String password;
    private final RoleUser role;
    private final String name;

    public static User create(String id, String password, RoleUser role, String name) {
        return new User(id, password, role, name);
    }

    private User(String id, String password, RoleUser role, String name) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.name = name;
    }
}
