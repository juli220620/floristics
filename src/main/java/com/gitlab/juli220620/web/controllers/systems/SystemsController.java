package com.gitlab.juli220620.web.controllers.systems;

import com.gitlab.juli220620.dao.repo.GameSystemRepo;
import com.gitlab.juli220620.facades.GameSystemFacade;
import com.gitlab.juli220620.service.GameSystemService;
import com.gitlab.juli220620.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/systems")
@RequiredArgsConstructor
public class SystemsController {

    private final AuthUtils authUtils;
    private final GameSystemFacade gameSystemFacade;
    private final GameSystemService gameSystemService;
    private final GameSystemRepo gameSystemRepo;

    @PostMapping("/buy")
    public ResponseEntity<Object> buyGameSystem(HttpServletRequest http, @RequestBody String systemId) {
        return authUtils.authorized(http, token -> {
            gameSystemFacade.buyGameSystem(token, systemId);
            return null;
        });
    }

    @PostMapping("/upgrade")
    public ResponseEntity<Object> upgradeGameSystem(HttpServletRequest http, @RequestBody String systemId) {
        return authUtils.authorized(http, token -> {
            gameSystemFacade.upgradeGameSystem(token, systemId);
            return null;
        });
    }

    @GetMapping
    public ResponseEntity<List<UserGameSystemDto>> getUserSystems(HttpServletRequest http) {
        return authUtils.authorized(http, token -> gameSystemFacade.getUserSystems(token).stream()
                .map(entity -> new UserGameSystemDto(entity, gameSystemService))
                .collect(Collectors.toList()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameSystemDto>> getAllSystems(HttpServletRequest http) {
        return authUtils.authorized(http, token ->
                gameSystemRepo.findAll().stream()
                        .map(entity -> new GameSystemDto(entity, gameSystemService))
                        .collect(Collectors.toList()));
    }
}
