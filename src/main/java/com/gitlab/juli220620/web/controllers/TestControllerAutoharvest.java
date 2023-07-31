package com.gitlab.juli220620.web.controllers;

import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.utils.AuthUtils;
import com.gitlab.juli220620.web.controllers.room.FlowerStateDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test/{userId}")
@RequiredArgsConstructor
public class TestControllerAutoharvest {

    private final AuthUtils authUtils;
    private final LoginService loginService;
    private final RoomFlowerRepo roomFlowerRepo;

    @GetMapping
    public ResponseEntity<List<FlowerStateDto>> getAutoHarvestFlowers(
            HttpServletRequest http,
            @PathVariable Long userId) {
        return authUtils.authorized(http, token ->
                roomFlowerRepo.findAllByAutoHarvest(loginService.findUserByToken(token))
                .stream()
                .map(FlowerStateDto::new)
                .collect(Collectors.toList())
        );
    }
}
