package com.gitlab.juli220620.web.controllers.user;

import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthUtils authUtils;
    private final LoginService loginService;

    @GetMapping("/room")
    public ResponseEntity<List<UserRoomDto>> getRooms(HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                loginService.findUserByToken(token).getUserRooms().stream()
                .map(UserRoomDto::new)
                .collect(Collectors.toList()));
    }
}
