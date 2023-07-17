package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.systems.PerennialFlowersGameSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;

@Service
@RequiredArgsConstructor
public class PlantingService {

    private final RoomFlowerRepo flowerRepo;
    private final BaseFlowerDictRepo baseFlowerDictRepo;
    private final PotDictRepo potDictRepo;

    private final AchievementService achievementService;

    private final PerennialFlowersGameSystem perennialFlowersGameSystem;

    public RoomFlowerEntity plantFlower(String flowerId, String potId, Integer cycles, UserRoomEntity room) {
        BaseFlowerDictEntity baseFlower = baseFlowerDictRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("No such flower"));
        PotDictEntity pot = potDictRepo.findById(potId)
                .orElseThrow(() -> new RuntimeException("No such pot"));

        return plantFlower(baseFlower, pot, cycles, room);
    }

    public RoomFlowerEntity plantFlower(BaseFlowerDictEntity baseFlower,
                                        PotDictEntity pot,
                                        Integer cycles,
                                        UserRoomEntity room) {

        int usedSpace = room.getFlowers().stream().mapToInt(flower -> flower.getBasePot().getSize()).sum();
        if (usedSpace + pot.getSize() > room.getArea()) throw new RuntimeException("Not enough space");

        LocalDateTime now = LocalDateTime.now();

        achievementService.processBolshieNadezhdy(room.getUser(), baseFlower);

        RoomFlowerEntity entity = new RoomFlowerEntity(
                0, 0, 0L,
                now,
                now,
                GROWING_STATUS,
                0L,
                null,
                null,
                null,
                room, baseFlower, pot
        );

        perennialFlowersGameSystem.processPerennialFlower(room.getUser(), entity, cycles);

        return flowerRepo.save(entity);
    }
}
