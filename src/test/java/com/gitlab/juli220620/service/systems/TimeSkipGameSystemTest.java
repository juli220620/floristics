package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gitlab.juli220620.service.SimulationService.DEAD_STATUS;
import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TimeSkipGameSystemTest {

    private TimeSkipGameSystem timeSkipGameSystem;
    @Spy
    private RoomFlowerEntity flower;

    @BeforeEach
    public void setup() {
        timeSkipGameSystem = new TimeSkipGameSystem(
                new SimulationService(Mockito.mock(RoomFlowerRepo.class))
        );

        BaseFlowerDictEntity baseFlower = Mockito.mock(BaseFlowerDictEntity.class);
        Mockito.doReturn(2).when(baseFlower).getWaterConsumption();
        Mockito.doReturn(3).when(baseFlower).getNutrientConsumption();
        Mockito.doReturn(5L).when(baseFlower).getGrowthTime();
        flower.setGrowth(0L);
        flower.setWater(10);
        flower.setNutrient(15);
        flower.setDeathTicks(0L);
        flower.setStatus(GROWING_STATUS);
        flower.setBaseFlower(baseFlower);
    }

    @Test
    public void skipTime_whenCalled_happyPass() {
        timeSkipGameSystem.skipTime(flower, 10);

        assertEquals(0, flower.getWater());
        assertEquals(0, flower.getNutrient());
        assertEquals(5L, flower.getGrowth());
        assertEquals(DEAD_STATUS, flower.getStatus());
        assertEquals(5L, flower.getDeathTicks());
    }

}