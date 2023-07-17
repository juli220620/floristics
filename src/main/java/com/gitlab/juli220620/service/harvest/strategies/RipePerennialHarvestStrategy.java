package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import org.springframework.stereotype.Component;

import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;
import static com.gitlab.juli220620.service.SimulationService.RIPE_STATUS;

@Component
public class RipePerennialHarvestStrategy extends RipeHarvestStrategy {

    public static final StrategyKey RIPE_PERENNIAL_STRATEGY_KEY = new StrategyKey(RIPE_STATUS, true);

    public RipePerennialHarvestStrategy(HarvestBonusService harvestBonusService,
                                        AchievementService achievementService,
                                        RoomFlowerRepo repo,
                                        PotCashbackGameSystem potCashbackGameSystem
    ) {
        super(harvestBonusService, achievementService, repo, potCashbackGameSystem);
    }

    @Override
    protected void postProcessFlower(RoomFlowerEntity flower) {
        flower.setCurrentCycle(flower.getCurrentCycle() + 1);

        if (flower.getCurrentCycle() > flower.getCycles()){
            super.postProcessFlower(flower);
        } else {
            flower.setGrowth(0L);
            flower.setStatus(GROWING_STATUS);
        }
    }

    @Override
    public StrategyKey getKey() {
        return RIPE_PERENNIAL_STRATEGY_KEY;
    }
}
