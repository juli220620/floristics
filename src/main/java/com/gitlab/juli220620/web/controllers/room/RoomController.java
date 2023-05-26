package com.gitlab.juli220620.web.controllers.room;

import com.gitlab.juli220620.facades.RoomFacade;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/room/{roomId}")
@RequiredArgsConstructor
public class RoomController {

    private final AuthUtils authUtils;
    private final RoomFacade roomFacade;

    @PostMapping
    public ResponseEntity<Object> plant(@RequestBody PlantFlowerRq rq,
                                        @PathVariable Long roomId,
                                        HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.updateRoom(roomId, token, LocalDateTime.now());
            roomFacade.plantFlower(token, rq.getBaseFlowerId(), rq.getPotId(), roomId);
            return null;
        });
    }

    @PostMapping("/water")
    public ResponseEntity<Object> water(@RequestBody TendFlowerRq rq,
                                        @PathVariable Long roomId,
                                        HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.updateRoom(roomId, token, LocalDateTime.now());
            roomFacade.waterFlower(token, rq.getFlowerId(), rq.getAmount());
            return null;
        });
    }

    @PostMapping("/feed")
    public ResponseEntity<Object> feed(@RequestBody TendFlowerRq rq,
                                       @PathVariable Long roomId,
                                       HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.updateRoom(roomId, token, LocalDateTime.now());
            roomFacade.feedFlower(token, rq.getFlowerId(), rq.getAmount());
            return null;
        });
    }

    @GetMapping("/harvest/{flowerId}")
    public ResponseEntity<Object> harvest(@PathVariable Long roomId,
                                          @PathVariable Long flowerId,
                                          HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.updateRoom(roomId, token, LocalDateTime.now());
            roomFacade.harvestFlower(token, flowerId);
            return null;
        });
    }

    @GetMapping
    public ResponseEntity<RoomStateDto> update(@PathVariable Long roomId,
                                               HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                new RoomStateDto(roomFacade.updateRoom(roomId, token, LocalDateTime.now())));
    }

    @PutMapping("/{additive}")
    public ResponseEntity<Object> update(@PathVariable Long roomId,
                                         @PathVariable Integer additive,
                                         HttpServletRequest http) {
        return authUtils.admin(http, token -> {
            roomFacade.updateRoom(roomId, token, LocalDateTime.now().plusMinutes(additive));
            return null;
        });
    }
}
