package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;

import java.util.Collections;
import java.util.Map;

public class DefaultHarvestStrategy implements HarvestStrategy {
    @Override
    public Map<String, Long> process(RoomFlowerEntity flower) {
        return Collections.emptyMap();
    }

    @Override
    public StrategyKey getKey() {
        return null;
    }
}
