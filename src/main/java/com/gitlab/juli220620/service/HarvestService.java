package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final RoomFlowerRepo flowerRepo;

    public Map<String, Integer> harvest(RoomFlowerEntity flower) {
        if (!flower.getStatus().contentEquals("RIPE")) return null;

        Map<String, Integer> harvestResult = new HashMap<>(flower.getBaseFlower().getHarvest());

        flowerRepo.delete(flower);

        return harvestResult;
    }
}
