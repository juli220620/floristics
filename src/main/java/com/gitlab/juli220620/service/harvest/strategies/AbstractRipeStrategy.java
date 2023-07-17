package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractRipeStrategy implements HarvestStrategy {

    private final HarvestBonusService harvestBonusService;
    private final AchievementService achievementService;

    public Map<String, Integer> process(RoomFlowerEntity flower) {

        Map<String, Integer> harvest = new HashMap<>(flower.getBaseFlower().getHarvest());

        achievementService.processMaminSadovod(flower.getRoom().getUser(), flower.getBaseFlower());

        Map<String, HarvestBonusEntity> bonuses = harvestBonusService.getHarvestBonuses(
                flower.getRoom().getUser(),
                flower.getBaseFlower()
        );

        bonuses.forEach((key, bonus) -> {
            if (!harvest.containsKey(key)) return;

            Integer baseHarvest = harvest.get(key);
            if (baseHarvest == null) return;

            Integer modifiedHarvest = (int) (baseHarvest * bonus.getMultiplier()) + bonus.getFlatBonus();
            harvest.put(key, modifiedHarvest);
        });

        postProcessFlower(flower);

        return harvest;
    }

    protected abstract void postProcessFlower(RoomFlowerEntity flower);
}
