package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SimulationService {
    public static final long SECONDS_IN_TICK = 60L;
    public static final String GROWING_STATUS = "GROWING";
    public static final String RIPE_STATUS = "RIPE";
    public static final String DEAD_STATUS = "DEAD";

    private final RoomFlowerRepo flowerRepo;

    public RoomFlowerEntity update(RoomFlowerEntity flower, LocalDateTime updateTime) {
//        if (!flower.getUpdated().isBefore(updateTime)) throw new RuntimeException("No time traveling allowed");
        if (flower.getStatus().equals(DEAD_STATUS)) return flower;

        long duration = ChronoUnit.SECONDS.between(flower.getUpdated(), updateTime);
        long ticksPassed = duration / SECONDS_IN_TICK;

        int waterConsumed = (int) (flower.getBaseFlower().getWaterConsumption() * ticksPassed);
        int nutrientConsumed = (int) (flower.getBaseFlower().getNutrientConsumption() * ticksPassed);

        int waterLeft = Math.max(0, (flower.getWater() - waterConsumed));
        int nutrientLeft = Math.max(0, (flower.getNutrient() - nutrientConsumed));

        checkDeathConditions(flower, ticksPassed, waterLeft, nutrientLeft);

        long currentGrowth = Math.min(flower.getBaseFlower().getGrowthTime(), Math.max((flower.getGrowth() + ticksPassed), 0));

        if (flower.getStatus().contentEquals(GROWING_STATUS)
                && flower.getBaseFlower().getGrowthTime() <= currentGrowth) {
            flower.setStatus(RIPE_STATUS);
        }

        flower.setWater(waterLeft);
        flower.setNutrient(nutrientLeft);
        flower.setGrowth(currentGrowth);
        flower.setUpdated(updateTime);

        return flowerRepo.save(flower);
    }

    private void checkDeathConditions(RoomFlowerEntity flower,
                                         long ticksPassed,
                                         int waterLeft,
                                         int nutrientLeft) {
        if (flower.getStatus().contentEquals(DEAD_STATUS)) return;
        if (waterLeft > 0 && nutrientLeft > 0) return;

        long canSurviveFor = flower.getBaseFlower().getGrowthTime() / 2;

        long capableWaterTicks = flower.getWater() / flower.getBaseFlower().getWaterConsumption();
        long capableNutrientTicks = flower.getNutrient() / flower.getBaseFlower().getNutrientConsumption();

        long thirstyTicks = ticksPassed - capableWaterTicks;
        long hungryTicks = ticksPassed - capableNutrientTicks;

        if (thirstyTicks > canSurviveFor || hungryTicks > canSurviveFor) {
            flower.setStatus(DEAD_STATUS);
        }
    }
}
