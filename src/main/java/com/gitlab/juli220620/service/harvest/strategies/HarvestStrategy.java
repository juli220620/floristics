package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;

import java.util.Map;

public interface HarvestStrategy {

    Map<String, Long> process(RoomFlowerEntity flower);

    StrategyKey getKey();
}
