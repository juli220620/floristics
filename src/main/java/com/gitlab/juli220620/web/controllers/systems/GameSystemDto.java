package com.gitlab.juli220620.web.controllers.systems;

import com.gitlab.juli220620.dao.entity.GameSystemEntity;
import com.gitlab.juli220620.service.GameSystemService;
import com.gitlab.juli220620.service.systems.GameSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameSystemDto {

    private String id;
    private String name;
    private Map<Integer, Map<String, Long>> levelCosts;

    public GameSystemDto(GameSystemEntity system, GameSystemService service) {
        this.id = system.getId();
        this.name = system.getName();
        this.levelCosts = Optional.ofNullable(service.getSystems().get(id))
                .map(GameSystem::getCost)
                .orElse(Collections.emptyMap());
    }
}
