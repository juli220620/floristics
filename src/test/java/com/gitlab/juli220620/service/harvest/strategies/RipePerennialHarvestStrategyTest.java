package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RipePerennialHarvestStrategyTest {

    @Mock
    private HarvestBonusService harvestBonusService;
    @Mock
    AchievementService achievementService;
    @Mock
    RoomFlowerRepo repo;
    @Mock
    PotCashbackGameSystem cashbackGameSystem;

    @Spy
    @InjectMocks
    private RipePerennialHarvestStrategy strategy;

    @Test
    public void postProcess_whenShouldContinueGrow_correctValues() {
        RoomFlowerEntity flower = createFlower(2, 3);

        strategy.postProcessFlower(flower);

        assertEquals(3, flower.getCurrentCycle());
        assertEquals(0L, flower.getGrowth());
        assertEquals(GROWING_STATUS, flower.getStatus());
    }

    @Test
    public void postProcess_whenShouldNotContinueGrow_notGrowing() {
        RoomFlowerEntity flower = createFlower(3, 3);

        Mockito.doReturn(Mockito.mock(UserRoomEntity.class)).when(flower).getRoom();

        strategy.postProcessFlower(flower);

        assertNull(flower.getGrowth());
        assertNull(flower.getStatus());
    }

    private RoomFlowerEntity createFlower(int currentCycles, int maxCycles) {
        RoomFlowerEntity flower = Mockito.spy(new RoomFlowerEntity());
        flower.setCurrentCycle(currentCycles);
        flower.setCycles(maxCycles);
        return flower;
    }

}