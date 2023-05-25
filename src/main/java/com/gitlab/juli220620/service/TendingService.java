package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TendingService {
    public static final Integer NUTRIENT_UNIT_COST = 1;
    public static final Integer WATER_UNIT_COST = 1;

    private final RoomFlowerRepo flowerRepo;

    public Integer water(Integer amount, RoomFlowerEntity flower) {
        return add(amount, flower, actualAmount -> flower.setWater(flower.getWater() + actualAmount));
    }

    public Integer feed(Integer amount, RoomFlowerEntity flower) {
        return add(amount, flower, actualAmount -> flower.setNutrient(flower.getNutrient() + actualAmount));
    }

    private Integer add(Integer amount, RoomFlowerEntity flower, Consumer<Integer> valueSettingStrategy) {
        int emptySpace = flower.getBasePot().getCapacity() - flower.getWater() - flower.getNutrient();
        int actualAmount = Math.min(emptySpace, amount);

        valueSettingStrategy.accept(actualAmount);
        flowerRepo.save(flower);

        return actualAmount;
    }
}
