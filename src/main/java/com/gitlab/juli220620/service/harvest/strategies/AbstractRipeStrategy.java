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

    protected final HarvestBonusService harvestBonusService;
    protected final AchievementService achievementService;

    public Map<String, Long> process(RoomFlowerEntity flower) {

        Map<String, Long> harvest = new HashMap<>(flower.getBaseFlower().getHarvest());

        achievementService.processMaminSadovod(flower.getRoom().getUser(), flower.getBaseFlower());

        Map<String, HarvestBonusEntity> bonuses = harvestBonusService.getHarvestBonuses(
                flower.getRoom().getUser(),
                flower.getBaseFlower()
        );

        bonuses.forEach((key, bonus) -> {
            if (!harvest.containsKey(key)) return;

            Long baseHarvest = harvest.get(key);
            if (baseHarvest == null) return;

            Long modifiedHarvest = (long) (baseHarvest * bonus.getMultiplier()) + bonus.getFlatBonus();
            harvest.put(key, modifiedHarvest);
        });

        flower.getRoom().getUser().getFlowerCount()
                .compute(flower.getBaseFlower().getId(), (s, value) -> value == null ? 1 : value + 1);

        postProcessFlower(flower);

        return harvest;
    }

    protected abstract void postProcessFlower(RoomFlowerEntity flower);
}
