package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;
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
        if (isFreeOffspringViable(flower)) {
            LocalDateTime now = LocalDateTime.now();
            flower.setGrowth(0L);
            flower.setUpdated(now);
            flower.setPlanted(now);
            flower.setStatus(GROWING_STATUS);
            flower.setDeathTicks(0L);
            roomFlowerRepo.save(flower);
        } else {
            potCashbackGameSystem.cashback(flower.getRoom().getUser(), flower.getBasePot());
            roomFlowerRepo.customDelete(flower.getId());
        }
    }

    private boolean isFreeOffspringViable(RoomFlowerEntity flower) {
        if (flower.getCycles() != null) return false;

        HarvestBonusEntity offspringChance = harvestBonusService.getMaxOffspringChance(flower).orElse(null);
        if (offspringChance == null) return false;
        if (offspringChance.getFreeOffspringChance() == null) return false;
        if (offspringChance.getFreeOffspringChance().equals(0.)) return false;

        return new Random().nextDouble() <= offspringChance.getFreeOffspringChance();
    }

    @Override
    public StrategyKey getKey() {
        return RIPE_STRATEGY_KEY;
    }
}
