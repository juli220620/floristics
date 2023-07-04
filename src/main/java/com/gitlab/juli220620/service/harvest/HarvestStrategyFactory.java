package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.service.harvest.strategies.DefaultHarvestStrategy;
import com.gitlab.juli220620.service.harvest.strategies.HarvestStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HarvestStrategyFactory {

    private final Map<String, HarvestStrategy> strategies;

    public HarvestStrategyFactory(List<HarvestStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(HarvestStrategy::getStatus, s -> s));
    }

    public HarvestStrategy getHarvestStrategy(String status) {
        return Optional.ofNullable(strategies.get(status)).orElse(new DefaultHarvestStrategy());
    }
}
