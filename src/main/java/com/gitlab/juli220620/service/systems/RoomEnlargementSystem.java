package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.CASH_ID;
import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.RED_ID;

@Component
@RequiredArgsConstructor
public class RoomEnlargementSystem implements GameSystem {
    public static final String ROOM_ENLARGEMENT_SYSTEM_ID = "ROOM_ENLARGEMENT";
    public transient final Map<Integer, Integer> MAX_ROOM_SIZES = Map.of(
            1, 7,
            2, 9,
            3, 10
    );

    public static final Long STONES_FOR_ROOM_ENLARGEMENT = 10L;

    private final UserRoomRepo userRoomRepo;

    public void enlargeRoom(UserRoomEntity room) {
        verifyRoomEnlargement(room);
        room.setArea(room.getArea() + 1);
        userRoomRepo.save(room);
    }

    public void verifyRoomEnlargement(UserRoomEntity room) {
        UserGameSystemEntity userSystem = room.getUser().getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(ROOM_ENLARGEMENT_SYSTEM_ID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("You don't have such power"));

        if (room.getArea() + 1 > MAX_ROOM_SIZES.get(userSystem.getSystemLevel()))
            throw new RuntimeException("Already the largest room");
    }

    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(
                1, Map.of(RED_ID, 2000000L),
                2, Map.of(
                        RED_ID, 5000000L,
                        CASH_ID, 5000000L
                ),
                3, Map.of(
                        RED_ID, 10000000L,
                        CASH_ID, 60000000L
                )
        );
    }

    @Override
    public String getId() {
        return ROOM_ENLARGEMENT_SYSTEM_ID;
    }
}
