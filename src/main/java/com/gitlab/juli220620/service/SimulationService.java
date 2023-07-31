package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.systems.AutoHarvestGameSystem;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimulationService {
    public static final long SECONDS_IN_TICK = 60L;
    public static final String GROWING_STATUS = "GROWING";
    public static final String RIPE_STATUS = "RIPE";
    public static final String DEAD_STATUS = "DEAD";

    private final RoomFlowerRepo flowerRepo;

    @Lazy
    @Autowired
    private AutoHarvestGameSystem autoHarvestGameSystem;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void update() {
        List<RoomFlowerEntity> flowers = flowerRepo.findAll().stream()
                .filter(it -> !it.getStatus().equals(DEAD_STATUS))
                .peek(it -> {
                    LocalDateTime now = now();
                    updateFlower(it, countNaturalUpdateTicks(it, now), now);
                })
                .collect(Collectors.toList());

        flowerRepo.saveAll(flowers);
    }

    public void updateFlower(RoomFlowerEntity flower, long ticksToAdd, LocalDateTime now) {
        int consumedWater = (int) (flower.getBaseFlower().getWaterConsumption() * ticksToAdd);
        int consumedNutrient = (int) (flower.getBaseFlower().getNutrientConsumption() * ticksToAdd);

        boolean alive = flower.getWater() >= consumedWater && flower.getNutrient() >= consumedNutrient;

        if (alive) {
            if (flower.getDeathTicks() > 0) {
                long carryOver = flower.getDeathTicks() - ticksToAdd;
                flower.setDeathTicks(Math.max(0, flower.getDeathTicks() - ticksToAdd));
                if (carryOver < 0) {
                    flower.setGrowth(flower.getGrowth() + carryOver * -1);
                }
            } else {
                flower.setGrowth(flower.getGrowth() + ticksToAdd);
            }
            flower.setWater(flower.getWater() - consumedWater);
            flower.setNutrient(flower.getNutrient() - consumedNutrient);
        } else {
            long growthBeforeDeath = calculateFlowerMaxGrowth(flower);
            flower.setGrowth(growthBeforeDeath);
            flower.setDeathTicks(flower.getDeathTicks() + (ticksToAdd - growthBeforeDeath));
            flower.setWater(Math.max(0, flower.getWater() - consumedWater));
            flower.setNutrient(Math.max(0, flower.getNutrient() - consumedNutrient));
        }

        boolean isGrowingStatus = flower.getStatus().equals(GROWING_STATUS);
        boolean isRipeStatus = flower.getStatus().equals(RIPE_STATUS);
        boolean isTimeToRipe = flower.getGrowth() >= flower.getBaseFlower().getGrowthTime();
        boolean isTimeToDie = flower.getDeathTicks() >= Math.max(1, flower.getBaseFlower().getGrowthTime() / 2);

        if (isGrowingStatus && isTimeToRipe) flower.setStatus(RIPE_STATUS);
        if ((isGrowingStatus || isRipeStatus) && isTimeToDie) flower.setStatus(DEAD_STATUS);

        flower.setUpdated(now);

        if (flower.getStatus().equals(RIPE_STATUS) && flower.isAutoHarvest()) {
            Hibernate.initialize(flower.getBaseFlower().getHarvest());
            autoHarvestGameSystem.autoHarvest(flower);
        }
    }

    private long countNaturalUpdateTicks(RoomFlowerEntity flower, LocalDateTime now) {
        long oldGrowthTicks = calculateTicks(flower.getPlanted(), flower.getUpdated());
        long newGrowthTicks = calculateTicks(flower.getPlanted(), now);
        return Math.max(0, newGrowthTicks - oldGrowthTicks);
    }

    private long calculateTicks(LocalDateTime past, LocalDateTime present) {
        long secondsPassed = ChronoUnit.SECONDS.between(past, present);
        return secondsPassed / SECONDS_IN_TICK;
    }

    private long calculateFlowerMaxGrowth(RoomFlowerEntity flower) {
        long growthOnWater = flower.getWater() / flower.getBaseFlower().getWaterConsumption();
        long growthOnNutrients = flower.getNutrient() / flower.getBaseFlower().getNutrientConsumption();
        return Math.min(growthOnWater, growthOnNutrients);
    }

    static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
