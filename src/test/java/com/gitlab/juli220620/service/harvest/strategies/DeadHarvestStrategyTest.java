package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeadHarvestStrategyTest {

    @Mock
    private RoomFlowerRepo roomFlowerRepo;
    @Spy
    @InjectMocks
    private DeadHarvestStrategy deadHarvestStrategy;

    @Test
    public void process_whenCalled_flowerDeletedAndEmptyResult() {
        RoomFlowerEntity mockFlower = Mockito.mock(RoomFlowerEntity.class);
        long flowerId = 1244;
        Mockito.doReturn(flowerId).when(mockFlower).getId();

        Map<String, Integer> result = deadHarvestStrategy.process(mockFlower);

        assertEquals(0, result.size());

        Mockito.verify(roomFlowerRepo, Mockito.times(1)).customDelete(flowerId);
    }
}