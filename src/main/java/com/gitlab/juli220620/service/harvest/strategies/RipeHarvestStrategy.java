package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import org.springframework.stereotype.Component;

import static com.gitlab.juli220620.service.SimulationService.RIPE_STATUS;

@Component
public class RipeHarvestStrategy extends AbstractRipeStrategy {

    public static final StrategyKey RIPE_STRATEGY_KEY = new StrategyKey(RIPE_STATUS, false);

    private final RoomFlowerRepo roomFlowerRepo;

    private final PotCashbackGameSystem potCashbackGameSystem;

    public RipeHarvestStrategy(HarvestBonusService harvestBonusService,
                               AchievementService achievementService,
                               RoomFlowerRepo repo,
                               PotCashbackGameSystem potCashbackGameSystem) {
        super(harvestBonusService, achievementService);
        roomFlowerRepo = repo;
        this.potCashbackGameSystem = potCashbackGameSystem;
    }

    @Override
    protected void postProcessFlower(RoomFlowerEntity flower) {
        potCashbackGameSystem.cashback(flower.getRoom().getUser(), flower.getBasePot());
        roomFlowerRepo.customDelete(flower.getId());
    }

    @Override
    public StrategyKey getKey() {
        return RIPE_STRATEGY_KEY;
    }
}
