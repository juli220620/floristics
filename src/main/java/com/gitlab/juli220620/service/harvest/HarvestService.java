package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final HarvestStrategyFactory strategyFactory;

    public Map<String, Integer> harvest(RoomFlowerEntity flower) {
        return strategyFactory
                .getHarvestStrategy(flower)
                .process(flower);
    }
}
