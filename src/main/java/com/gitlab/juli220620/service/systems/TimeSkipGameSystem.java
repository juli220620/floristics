package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.CASH_ID;
import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.RED_ID;

@Component
@RequiredArgsConstructor
public class TimeSkipGameSystem implements GameSystem {

    public static final String TIME_SKIP_SYSTEM_ID = "TIME_SKIP";

    static final Map<Integer, Integer> TICKS_EXCHANGE_RATE = Map.of(
            1, 100,
            2, 80,
            3, 75
    );

    private final SimulationService simulationService;
    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(
                1, Map.of(
                        CASH_ID, 50000L,
                        RED_ID, 20000L),
                2, Map.of(
                        CASH_ID, 700000L,
                        RED_ID, 100000L),
                3, Map.of(
                        CASH_ID, 1000000L));
    }

    public void skipTime(RoomFlowerEntity flower, long ticksToSkip) {
        simulationService.updateFlower(flower, ticksToSkip, LocalDateTime.now());
    }

    public long calculatePrice(UserGameSystemEntity entity, long ticksToSkip) {
        return ticksToSkip * TICKS_EXCHANGE_RATE.get(entity.getSystemLevel());
    }

    @Override
    public String getId() {
        return TIME_SKIP_SYSTEM_ID;
    }
}
