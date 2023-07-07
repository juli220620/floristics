package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.gitlab.juli220620.service.SimulationService.RIPE_STATUS;

@Component
@RequiredArgsConstructor
public class RipeHarvestStrategy implements HarvestStrategy {

    private final RoomFlowerRepo roomFlowerRepo;

    private final HarvestBonusService harvestBonusService;
    private final AchievementService achievementService;

    private final PotCashbackGameSystem potCashbackGameSystem;

    @Override
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

        potCashbackGameSystem.cashback(flower.getRoom().getUser(), flower.getBasePot());
        roomFlowerRepo.customDelete(flower.getId());

        return harvest;
    }

    @Override
    public String getStatus() {
        return RIPE_STATUS;
    }
}
