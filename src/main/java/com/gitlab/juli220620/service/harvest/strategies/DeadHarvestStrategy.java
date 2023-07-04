package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.gitlab.juli220620.service.SimulationService.DEAD_STATUS;

@Component
@RequiredArgsConstructor
public class DeadHarvestStrategy implements HarvestStrategy {

    private final RoomFlowerRepo roomFlowerRepo;

    @Override
    public Map<String, Integer> process(RoomFlowerEntity flower) {
        roomFlowerRepo.customDelete(flower.getId());
        return Collections.emptyMap();
    }

    @Override
    public String getStatus() {
        return DEAD_STATUS;
    }
}
