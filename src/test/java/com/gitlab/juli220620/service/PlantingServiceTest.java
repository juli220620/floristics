package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.systems.PerennialFlowersGameSystem;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlantingServiceTest {

    @Spy
    @InjectMocks
    PlantingService plantingService;

    @Mock
    RoomFlowerEntity flowerInList;
    @Mock
    BaseFlowerDictEntity baseFlower;
    @Mock
    PotDictEntity pot;
    @Mock
    UserRoomEntity room;
    @Mock
    RoomFlowerRepo repo;
    @Mock
    AchievementService achievementService;
    @Mock
    PerennialFlowersGameSystem perennialFlowersGameSystem;

    @Test
    public void plantFlower_whenCalled_happyPass() {
        Mockito.doReturn(Collections.emptyList()).when(room).getFlowers();
        Mockito.doReturn(5).when(room).getArea();
        Mockito.doReturn(1).when(pot).getSize();

        plantingService.plantFlower(baseFlower, pot, null, room);

        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(RoomFlowerEntity.class));
    }

    @Test
    public void plantFlower_whenRoomIsFull_error() {
        Mockito.doReturn(5).when(pot).getSize();
        Mockito.doReturn(pot).when(flowerInList).getBasePot();
        Mockito.doReturn(List.of(flowerInList)).when(room).getFlowers();

        Exception e = assertThrows(
                Exception.class,
                () -> plantingService.plantFlower(baseFlower, pot, null, room));

        assertEquals("Not enough space", e.getMessage());
    }
}