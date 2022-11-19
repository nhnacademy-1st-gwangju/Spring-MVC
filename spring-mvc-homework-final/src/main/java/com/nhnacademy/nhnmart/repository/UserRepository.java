package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.RoleUser;
import com.nhnacademy.nhnmart.domain.User;

public interface UserRepository {
    boolean exists(String id);
    boolean matches(String id, String password);
    User getUser(String id);
    User addUser(String id, String password, RoleUser role, String name);
}
