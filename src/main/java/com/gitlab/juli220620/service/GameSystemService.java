package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.GameSystemEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.entity.identity.UserGameSystemId;
import com.gitlab.juli220620.dao.repo.GameSystemRepo;
import com.gitlab.juli220620.service.systems.GameSystem;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameSystemService {

    @Getter
    private final Map<String, GameSystem> systems;
    private final GameSystemRepo gameSystemRepo;

    public GameSystemService(List<GameSystem> systems,
                             GameSystemRepo gameSystemRepo) {
        this.gameSystemRepo = gameSystemRepo;
        this.systems = systems.stream().collect(Collectors.toMap(
                GameSystem::getId,
                gameSystem -> gameSystem
        ));
    }

    /**
     * <p>Add system to user on 1 level and returns map of costs by currency.
     * <b>It does not spend any user currencies by itself</b></p>
     * @param user - recipient of system
     * @param systemId - system to receive
     * @return Map of upgrade costs by currency to subtract from user funds
     */
    public Map<String, Integer> addSystem(UserEntity user, String systemId) {
        GameSystemEntity system = gameSystemRepo.findById(systemId).orElseThrow();

        user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(systemId))
                .findFirst()
                .ifPresent(s -> { throw new RuntimeException("Already bought!"); });

        GameSystem systemToAdd = systems.get(systemId);

        user.getWorkingSystems().add(
                new UserGameSystemEntity(
                        new UserGameSystemId(user.getId(), null),
                        1,
                        system));

        return Optional.ofNullable(systemToAdd.getCost().get(1))
                .orElseThrow(() -> new RuntimeException(String.format("System [%s] is not for sale :D", systemId)));
    }

    /**
     * <p>Upgrades user active system by adding 1 level to it and returns map of costs for upgrade by currency.
     * <b>It does not spend any user currencies by itself</b></p>
     * @param user - recipient of upgrade
     * @param systemId - system to upgrade for recipient
     * @return Map of upgrade costs by currency to subtract from user funds
     */
    public Map<String, Integer> upgradeSystem(UserEntity user, String systemId) {
        UserGameSystemEntity userSystem = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(systemId))
                .findFirst()
                .orElseThrow();

        GameSystem target = systems.get(systemId);
        Map<String, Integer> costs = Optional.ofNullable(target.getCost().get(userSystem.getSystemLevel() + 1))
                .orElseThrow(() -> new RuntimeException("Max level"));

        userSystem.setSystemLevel(userSystem.getSystemLevel() + 1);

        return costs;
    }
}
