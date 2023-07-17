package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.service.harvest.strategies.DefaultHarvestStrategy;
import com.gitlab.juli220620.service.harvest.strategies.HarvestStrategy;
import com.gitlab.juli220620.service.harvest.strategies.StrategyKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HarvestStrategyFactory {

    private final Map<StrategyKey, HarvestStrategy> strategies;

    public HarvestStrategyFactory(List<HarvestStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(HarvestStrategy::getKey, s -> s));
    }

    public HarvestStrategy getHarvestStrategy(RoomFlowerEntity flower) {
        return Optional.ofNullable(strategies.get(new StrategyKey(flower)))
                .orElse(new DefaultHarvestStrategy());
    }
}
