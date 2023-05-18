package com.gitlab.juli220620.web.controllers.login;

import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public void login(@RequestBody LoginRq rq, HttpServletResponse response) {
            String token = loginService.authorize(rq.getUsername(), rq.getPassword());
        AuthUtils.setToken(response, token);
    }
}
