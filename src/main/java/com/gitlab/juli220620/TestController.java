package com.gitlab.juli220620;

import com.gitlab.juli220620.dao.repo.*;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final BaseFlowerDictRepo flowerRepo;
    private final PotDictRepo potRepo;
    private final RoomFlowerRepo roomFlowerRepo;
    private final UserRepo userRepo;
    private final UserRoomRepo userRoomRepo;
    private final AuthUtils authenticate;


    @GetMapping
    public ResponseEntity<String> testAuthentication(HttpServletRequest rq) {
        return authenticate.authorized(rq, token -> "Ok");
    }

}
