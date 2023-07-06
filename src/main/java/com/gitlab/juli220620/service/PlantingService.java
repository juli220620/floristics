package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
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

    public RoomFlowerEntity plantFlower(String flowerId, String potId, UserRoomEntity room) {
        BaseFlowerDictEntity baseFlower = baseFlowerDictRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("No such flower"));
        PotDictEntity pot = potDictRepo.findById(potId)
                .orElseThrow(() -> new RuntimeException("No such pot"));

        return plantFlower(baseFlower, pot, room);
    }

    public RoomFlowerEntity plantFlower(BaseFlowerDictEntity baseFlower, PotDictEntity pot, UserRoomEntity room) {
        LocalDateTime now = LocalDateTime.now();
        achievementService.processBolshieNadezhdy(room.getUser(), baseFlower);
        return flowerRepo.save(new RoomFlowerEntity(
                0, 0, 0L,
                now,
                now,
                GROWING_STATUS,
                0L,
                null,
                room, baseFlower, pot
        ));
    }
}
