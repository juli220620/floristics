package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.RED_ID;

@Component
public class PerennialFlowersGameSystem implements GameSystem {

    public static final String ID = "PERENNIAL_FLOWERS";

    public static final Map<Integer, Integer> MAX_CYCLES = Map.of(
            1, 2,
            2, 3,
            3, 4);

    public void processPerennialFlower(UserEntity user, RoomFlowerEntity flower, Integer cycles) {
        if (cycles == null || cycles <= 1) return;
        UserGameSystemEntity perennialFlower = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(ID))
                .findFirst().orElseThrow(() -> new RuntimeException("You can't do that."));

        if (cycles > MAX_CYCLES.get(perennialFlower.getSystemLevel()) ||
                cycles > flower.getBaseFlower().getMaxCycles())
            throw new RuntimeException("Too many cycles");

        flower.setCycles(cycles);
        flower.setCurrentCycle(1);
    }

    public Long modifyPrice(Long amount, RoomFlowerEntity flower) {
        if (flower == null || flower.getCycles() == null || flower.getCycles() <= 1) return amount;
        return (long) (amount + amount*(0.01 * flower.getCycles()));
    }

    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(
                1, Map.of(RED_ID, 20000L),
                2, Map.of(RED_ID, 50000L),
                3, Map.of(RED_ID, 100000L)
        );
    }

    @Override
    public String getId() {
        return ID;
    }
}
