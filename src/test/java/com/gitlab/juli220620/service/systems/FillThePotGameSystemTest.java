package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.service.TendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.BLUE_ID;
import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.GREEN_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FillThePotGameSystemTest {

    @Spy
    @InjectMocks
    private FillThePotGameSystem fillThePotGameSystem;
    @Spy
    private RoomFlowerEntity flower;

    @Mock
    private TendingService tendingService;
    @Mock
    private BaseFlowerDictEntity baseFlower;
    @Mock
    private PotDictEntity pot;

    @BeforeEach
    public void setupFlower() {
        flower.setBaseFlower(baseFlower);
        flower.setBasePot(pot);
    }

    @Test
    public void fillThePot_whenCalled_tendsFlower() {
        fillThePotGameSystem.fillThePot(flower, 50, 30);

        Mockito.verify(tendingService, Mockito.times(1)).water(50, flower);
        Mockito.verify(tendingService, Mockito.times(1)).feed(30, flower);
    }

    @ParameterizedTest
    @CsvSource({
            "35,60,80,0,0",
            "100,20,50,20,50",
            "200,100,100,100,100",
            "500,40,70,160,280"
    })
    public void getFillingAmount_whenCalled_correctValues(
            int potCapacity,
            int waterConsumption,
            int nutrientConsumption,
            int expectedWater,
            int expectedNutrients
    ) {
        setupParams(potCapacity, waterConsumption, nutrientConsumption);
        Map<String, Integer> costs = fillThePotGameSystem.getFillingAmount(flower);

        assertEquals(expectedWater, costs.get(BLUE_ID));
        assertEquals(expectedNutrients, costs.get(GREEN_ID));
    }

    private void setupParams(int potCapacity, int waterConsumption, int nutrientConsumption) {
        Mockito.doReturn(potCapacity).when(pot).getCapacity();
        Mockito.doReturn(waterConsumption).when(baseFlower).getWaterConsumption();
        Mockito.doReturn(nutrientConsumption).when(baseFlower).getNutrientConsumption();
    }
}