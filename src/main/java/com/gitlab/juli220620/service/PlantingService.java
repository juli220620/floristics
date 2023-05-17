package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlantingService {

    private final RoomFlowerRepo flowerRepo;

    public RoomFlowerEntity plantFlower(BaseFlowerDictEntity baseFlower, PotDictEntity pot, UserRoomEntity room) {
        return flowerRepo.save(new RoomFlowerEntity(
                null,
                0, 0, 0L,
                LocalDateTime.now(),
                "GROWING",
                room,
                baseFlower,
                pot
        ));
    }
}
