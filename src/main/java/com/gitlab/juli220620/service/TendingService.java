package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TendingService {

    private final RoomFlowerRepo flowerRepo;

    public Integer water(Integer amount, RoomFlowerEntity flower) {
        return add(amount, flower, flower::setWater);
    }

    public Integer feed(Integer amount, RoomFlowerEntity flower) {
        return add(amount, flower, flower::setNutrient);
    }

    private Integer add(Integer amount, RoomFlowerEntity flower, Consumer<Integer> valueSettingStrategy) {
        int emptySpace = flower.getBasePot().getCapacity() - flower.getWater() - flower.getNutrient();
        int actualAmount = Math.min(emptySpace, amount);

        valueSettingStrategy.accept(actualAmount);
        flowerRepo.update(flower);

        return actualAmount;
    }
}
