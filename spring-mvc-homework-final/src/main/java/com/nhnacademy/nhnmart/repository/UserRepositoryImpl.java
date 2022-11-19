package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.RoleUser;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserAlreadyExistsException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    @Override
    public boolean exists(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable(getUser(id))
                       .map(user -> user.getPassword().equals(password))
                       .orElse(false);
    }

    @Override
    public User getUser(String id) {
        return exists(id) ? userMap.get(id) : null;
    }

    @Override
    public User addUser(String id, String password, RoleUser role, String name) {
        if (exists(id)) {
            throw new UserAlreadyExistsException(id);
        }

        User user = User.create(id, password, role, name);

        userMap.put(id, user);

        return user;
    }
}
