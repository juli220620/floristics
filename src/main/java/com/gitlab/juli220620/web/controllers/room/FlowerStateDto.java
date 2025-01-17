package com.gitlab.juli220620.web.controllers.room;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlowerStateDto {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long id;
    private String baseFlowerId;
    private String potId;

    private Integer water;
    private Integer nutrient;

    private String status;
    private Long growth;
    private Long ticksToRipe;
    private Long deathTicks;
    private String lastUpdate;
    private String planted;
    private Integer currentCycle;
    private Integer cycles;
    private boolean autoHarvest;

    public FlowerStateDto(RoomFlowerEntity flower) {
        id = flower.getId();
        baseFlowerId = flower.getBaseFlower().getId();
        potId = flower.getBasePot().getId();

        water = flower.getWater();
        nutrient = flower.getNutrient();

        status = flower.getStatus();
        growth = flower.getGrowth();
        ticksToRipe = flower.getBaseFlower().getGrowthTime();
        deathTicks = flower.getDeathTicks();
        lastUpdate = flower.getUpdated().format(formatter);
        planted = flower.getPlanted().format(formatter);
        currentCycle = flower.getCurrentCycle();
        cycles = flower.getCycles();
        autoHarvest = flower.isAutoHarvest();
    }
}
