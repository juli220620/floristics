package com.gitlab.juli220620.web.controllers.room;

import com.gitlab.juli220620.facades.RoomFacade;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
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
            roomFacade.plantFlower(token,
                    rq.getBaseFlowerId(),
                    rq.getPotId(),
                    rq.getCycles(),
                    rq.isAutoHarvest(),
                    rq.isNeedFilling(),
                    roomId
            );
            return null;
        });
    }

    @PostMapping("/water")
    public ResponseEntity<Object> water(@RequestBody TendFlowerRq rq,
                                        @PathVariable Long roomId,
                                        HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.waterFlower(token, rq.getFlowerId(), rq.getAmount());
            return null;
        });
    }

    @PostMapping("/feed")
    public ResponseEntity<Object> feed(@RequestBody TendFlowerRq rq,
                                       @PathVariable Long roomId,
                                       HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.feedFlower(token, rq.getFlowerId(), rq.getAmount());
            return null;
        });
    }

    @GetMapping("/harvest/{flowerId}")
    public ResponseEntity<Object> harvest(@PathVariable Long roomId,
                                          @PathVariable Long flowerId,
                                          HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.harvestFlower(token, flowerId);
            return null;
        });
    }

    @PostMapping("/skip")
    public ResponseEntity<Object> skipTime(@PathVariable Long roomId,
                                           @RequestBody SkipTimeRq rq,
                                           HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
                roomFacade.skipTime(token, rq.getFlowerId(), rq.getTicksToSkip());
                return null;
        });
    }

    @PatchMapping("/enlarge")
    public ResponseEntity<Object> enlargeRoom(@PathVariable Long roomId, HttpServletRequest http) {
        return authUtils.authorized(http, token -> {
            roomFacade.enlargeRoom(token, roomId);
            return null;
        });
    }

    @GetMapping
    public ResponseEntity<RoomStateDto> getRoomState(@PathVariable Long roomId,
                                                     HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                new RoomStateDto(roomFacade.getRoom(roomId, token)));
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<FlowerStateDto> getFlowerState(@PathVariable Long roomId,
                                                         @PathVariable Long flowerId,
                                                         HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                new FlowerStateDto(roomFacade.getFlower(roomId, flowerId, token)));
    }
}
