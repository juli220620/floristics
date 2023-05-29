package com.gitlab.juli220620.web.controllers.user;

import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.service.WalletService;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthUtils authUtils;
    private final LoginService loginService;
    private final WalletService walletService;

    @GetMapping("/room")
    public ResponseEntity<List<UserRoomDto>> getRooms(HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                loginService.findUserByToken(token).getUserRooms().stream()
                .map(UserRoomDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(HttpServletRequest http) {
        return authUtils.authorized(http, token -> new UserDto(loginService.findUserByToken(token)));
    }

    @PostMapping("/sell")
    public ResponseEntity<Object> exchangeForCash(HttpServletRequest http, @RequestBody SellCurrencyRq rq) {
        return authUtils.authorized(http, token -> {
                    walletService.sell(rq.getAmount(), rq.getCurrencyId(), loginService.findUserByToken(token));
                    return null;
                });
    }
}
