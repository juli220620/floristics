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

    public static final Map<Integer, Integer> maxCycles = Map.of(
            1, 2,
            2, 3,
            3, 4);

    public void processPerennialFlower(UserEntity user, RoomFlowerEntity flower, Integer cycles) {
        if (cycles == null || cycles <= 1) return;
        UserGameSystemEntity perennialFlower = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(ID))
                .findFirst().orElseThrow(() -> new RuntimeException("You can't do that."));

        if (cycles > maxCycles.get(perennialFlower.getSystemLevel()) ||
                cycles > flower.getBaseFlower().getMaxCycles())
            throw new RuntimeException("Too many cycles");

        flower.setCycles(cycles);
        flower.setCurrentCycle(1);
    }

    public Integer modifyPrice(Integer amount, RoomFlowerEntity flower) {
        if (flower == null || flower.getCycles() == null || flower.getCycles() <= 1) return amount;
        return (int) (amount + amount*(0.01 * flower.getCycles()));
    }

    @Override
    public Map<Integer, Map<String, Integer>> getCost() {
        return Map.of(
                1, Map.of(RED_ID, 20000),
                2, Map.of(RED_ID, 50000),
                3, Map.of(RED_ID, 100000)
        );
    }

    @Override
    public String getId() {
        return ID;
    }
}
