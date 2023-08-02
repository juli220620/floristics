package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.entity.identity.UserGameSystemId;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import com.gitlab.juli220620.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.STONES_AND_BOARDS_ID;
import static com.gitlab.juli220620.service.systems.RoomEnlargementSystem.STONES_FOR_ROOM_ENLARGEMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoomEnlargementSystemTest {

    @Spy
    @InjectMocks
    private RoomEnlargementSystem roomEnlargementSystem;
    @Spy
    private UserRoomEntity room;

    @Mock
    private UserRoomRepo roomRepo;
    @Mock
    private WalletService walletService;
    @Mock
    private UserEntity user;

    @BeforeEach
    public void setup() {
        room.setArea(5);
        room.setUser(user);
    }

    @Test
    public void enlargeRoom_whenVerified_happyPass() {
        setSystem();
        Mockito.doReturn(true).when(walletService)
                .spend(STONES_FOR_ROOM_ENLARGEMENT, STONES_AND_BOARDS_ID, user);
        roomEnlargementSystem.enlargeRoom(room);

        assertEquals(6, room.getArea());
    }

    @Test
    public void verifyRoomEnlargement_whenNoSystem_throwsException() {
        Mockito.doReturn(Collections.emptyList()).when(user).getWorkingSystems();
        evaluateException("You don't have such power");
    }

    @Test
    public void verifyRoomEnlargement_whenMaxSizeAlready_throwsException() {
        setSystem();
        room.setArea(7);
        evaluateException("Already the largest room");
    }

    private void evaluateException(String expected) {
        Exception e = assertThrows(Exception.class, () -> roomEnlargementSystem.verifyRoomEnlargement(room));
        assertEquals(expected, e.getMessage());
    }

    private void setSystem() {
        UserGameSystemEntity userSystem = Mockito.mock(UserGameSystemEntity.class);
        UserGameSystemId systemId = Mockito.mock(UserGameSystemId.class);

        Mockito.doReturn(1).when(userSystem).getSystemLevel();
        Mockito.doReturn(roomEnlargementSystem.getId()).when(systemId).getSystemId();
        Mockito.doReturn(systemId).when(userSystem).getId();
        Mockito.doReturn(List.of(userSystem)).when(user).getWorkingSystems();
    }
}