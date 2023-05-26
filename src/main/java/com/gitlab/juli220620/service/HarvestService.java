package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final RoomFlowerRepo flowerRepo;

    public Map<String, Integer> harvest(RoomFlowerEntity flower) {
        Map<String, Integer> harvestResult = new HashMap<>(
                flower.getStatus().contentEquals("RIPE")
                        ? flower.getBaseFlower().getHarvest()
                        : Collections.emptyMap()
        );

        flowerRepo.customDelete(flower.getId());

        return harvestResult;
    }
}
