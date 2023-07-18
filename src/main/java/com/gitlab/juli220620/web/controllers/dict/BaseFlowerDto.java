package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseFlowerDto {

    private String id;
    private String name;
    private Long growthTime;
    private Integer waterConsumption;
    private Integer nutrientConsumption;
    private Integer price;
    private Integer maxCycles;

    private Map<String, Integer> harvest;

    private List<HarvestBonusDto> harvestBonuses;

    public BaseFlowerDto(BaseFlowerDictEntity entity) {
        id = entity.getId();
        name = entity.getName();
        growthTime = entity.getGrowthTime();
        waterConsumption = entity.getWaterConsumption();
        nutrientConsumption = entity.getNutrientConsumption();
        price = entity.getPrice();
        maxCycles = entity.getMaxCycles();

        harvest = new HashMap<>();
        harvest.putAll(entity.getHarvest());

        harvestBonuses = new ArrayList<>();
        harvestBonuses.addAll(entity.getHarvestBonuses().stream()
                .map(HarvestBonusDto::new)
                .toList());
    }
}
