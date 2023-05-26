package com.gitlab.juli220620.utils;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthUtils {
    private static final List<String> adminWhitelist = List.of("cvazer", "julia-flower-girl");
    private final LoginService loginService;

    public static void setToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static String extractToken(HttpServletRequest rq) {
        return Arrays.stream(Optional.ofNullable(rq.getCookies()).orElse(new Cookie[]{}))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Fuck you from Smokin' Joe"));
    }

    public <T> ResponseEntity<T> authorized(HttpServletRequest rq, Function<String, T> strategy) {
        String token = extractToken(rq);
        if (!loginService.authenticate(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(strategy.apply(token), HttpStatus.OK);
    }

    public <T> ResponseEntity<T> admin(HttpServletRequest rq, Function<String, T> strategy) {
        String token = extractToken(rq);
        UserEntity user = loginService.findUserByToken(token);
        if (!loginService.authenticate(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!adminWhitelist.contains(user.getUsername())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(strategy.apply(token), HttpStatus.OK);
    }
}
