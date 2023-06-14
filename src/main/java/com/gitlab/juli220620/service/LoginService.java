package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepo userRepo;
    private final Map<String, String> tokens = new HashMap<>();

    public String authorize(String username, String password) {
        UserEntity entity = fetchUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Wrong username"));

        if (!entity.getPassword().contentEquals(password)) throw new RuntimeException("Wrong password");

        String token = generateToken();
        tokens.put(token, username);

        return token;
    }

    public boolean authenticate(String token) {
        return tokens.containsKey(token);
    }

    public UserEntity findUserByToken(String token) {
        if (!tokens.containsKey(token)) return null;
        return fetchUserByUsername(tokens.get(token)).orElseThrow();
    }

    public boolean checkUserToken(String token, UserEntity user) {
        return user.getUsername().equals(tokens.get(token));
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private Optional<UserEntity> fetchUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(it -> it.getUsername().equals(username) ? it : null);
    }
}
