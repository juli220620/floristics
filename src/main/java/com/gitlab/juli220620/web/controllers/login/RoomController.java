package com.gitlab.juli220620.web.controllers.login;

import com.gitlab.juli220620.facades.RoomFacade;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room/{roomId}")
@RequiredArgsConstructor
public class RoomController {

    private final AuthUtils authUtils;
    private final RoomFacade roomFacade;

    @PostMapping
    public ResponseEntity<Object> plant(@RequestBody PlantFlowerRq rq, @PathVariable Long roomId, HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.plantFlower(token, rq.getBaseFlowerId(), rq.getPotId(), roomId);
            return null;
        });
    }
}
