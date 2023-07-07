package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.gitlab.juli220620.service.SimulationService.DEAD_STATUS;

@Component
@RequiredArgsConstructor
public class DeadHarvestStrategy implements HarvestStrategy {

    private final RoomFlowerRepo roomFlowerRepo;
    private final PotCashbackGameSystem potCashbackGameSystem;

    @Override
    public Map<String, Integer> process(RoomFlowerEntity flower) {
        potCashbackGameSystem.cashback(flower.getRoom().getUser(), flower.getBasePot());
        roomFlowerRepo.customDelete(flower.getId());
        return Collections.emptyMap();
    }

    @Override
    public String getStatus() {
        return DEAD_STATUS;
    }
}
