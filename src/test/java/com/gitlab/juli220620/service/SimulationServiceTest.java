package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.gitlab.juli220620.service.SimulationService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SimulationServiceTest {

    @Spy
    @InjectMocks
    private SimulationService simulation;

    @Mock
    RoomFlowerRepo repo;

    @Spy
    private RoomFlowerEntity testFlower = Mockito.spy(new RoomFlowerEntity(
            100, 100, 0L,
            null, null,
            GROWING_STATUS,
            0L,
            null, null, null, null));

    @Mock
    private BaseFlowerDictEntity baseFlowerOne;
    @Mock
    private BaseFlowerDictEntity baseFlowerTwo;
    @Mock
    private BaseFlowerDictEntity baseFlowerTen;

    private static final MockedStatic<SimulationService> simulationServiceMockedStatic = Mockito.mockStatic(SimulationService.class);

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    public void setup() {
        Mockito.doReturn(Collections.singletonList(testFlower)).when(repo).findAll();

        Mockito.doReturn(10).when(baseFlowerOne).getWaterConsumption();
        Mockito.doReturn(10).when(baseFlowerTwo).getWaterConsumption();
        Mockito.doReturn(10).when(baseFlowerTen).getWaterConsumption();
        Mockito.doReturn(20).when(baseFlowerOne).getNutrientConsumption();
        Mockito.doReturn(20).when(baseFlowerTwo).getNutrientConsumption();
        Mockito.doReturn(20).when(baseFlowerTen).getNutrientConsumption();

        Mockito.doReturn(1L).when(baseFlowerOne).getGrowthTime();
        Mockito.doReturn(2L).when(baseFlowerTwo).getGrowthTime();
        Mockito.doReturn(10L).when(baseFlowerTen).getGrowthTime();
    }

    public void setupDates(String plantedDate, String nowDate) {
        LocalDateTime time = LocalDateTime.parse(plantedDate, formatter);
        testFlower.setPlanted(time);
        testFlower.setUpdated(time);
        simulationServiceMockedStatic.when(SimulationService::now).thenReturn(LocalDateTime.parse(nowDate, formatter));
    }

    @ParameterizedTest
    @CsvSource({
            "2000-01-01 00:00:00,2000-01-01 00:01:00,"+RIPE_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:00:30,"+GROWING_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:01:30,"+RIPE_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:02:00,"+RIPE_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:10:00,"+DEAD_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:06:00,"+DEAD_STATUS,
            "2000-01-01 00:00:00,2000-01-01 00:05:00,"+RIPE_STATUS

    })
    public void updateFlower_whenTimePasses_correctStatus(String planted, String now, String expectedStatus) {
        setupDates(planted, now);
        Mockito.doReturn(baseFlowerOne).when(testFlower).getBaseFlower();

        simulation.update();

        assertEquals(expectedStatus, testFlower.getStatus());
    }

    @Test
    public void updateFlower_whenNegativeTimePasses_growthPositive() {
        setupDates("2000-01-01 00:01:00", "2000-01-01 00:00:00");
        Mockito.doReturn(baseFlowerOne).when(testFlower).getBaseFlower();

        simulation.update();

        assertEquals(0, testFlower.getGrowth());
    }

    @Test
    public void updateFlower_whenNegativeTimePasses_deathTicksNotGrowing() {
        setupDates("2000-01-01 00:01:00", "2000-01-01 00:00:00");
        Mockito.doReturn(baseFlowerTen).when(testFlower).getBaseFlower();
        testFlower.setDeathTicks(3L);

        simulation.update();

        assertEquals(3L, testFlower.getDeathTicks());
    }

    @Test
    public void updateFlower_whenEnoughNutrition_deathTicksReduce() {
        setupDates("2000-01-01 00:00:00", "2000-01-01 00:01:00");
        Mockito.doReturn(baseFlowerTen).when(testFlower).getBaseFlower();
        testFlower.setDeathTicks(3L);

        simulation.update();

        assertEquals(2L, testFlower.getDeathTicks());
        assertEquals(0, testFlower.getGrowth());
    }

    @Test
    public void updateFlower_whenSwitchingFromWitheringToGrowing_correctValues() {
        setupDates("2000-01-01 00:00:00", "2000-01-01 00:01:00");
        Mockito.doReturn(baseFlowerTen).when(testFlower).getBaseFlower();
        testFlower.setDeathTicks(1L);

        simulation.update();
        assertEquals(0L, testFlower.getDeathTicks());
        assertEquals(0L, testFlower.getGrowth());

        simulationServiceMockedStatic.when(SimulationService::now)
                .thenReturn(LocalDateTime.parse("2000-01-01 00:02:00", formatter));
        simulation.update();

        assertEquals(0L, testFlower.getDeathTicks());
        assertEquals(1L, testFlower.getGrowth());
    }

    @ParameterizedTest
    @CsvSource({
            "2000-01-01 00:00:00,2000-01-01 00:01:00,90,80",
            "2000-01-01 00:00:00,2000-01-01 00:05:00,50,0",
            "2000-01-01 00:00:00,2000-01-01 00:10:00,0,0"

    })
    public void updateFlower_whenNutritionConsumed_correctValues(
            String planted, String now,
            int expectedWater, int expectedNutrients) {
        setupDates(planted, now);
        Mockito.doReturn(baseFlowerTen).when(testFlower).getBaseFlower();

        simulation.update();

        assertEquals(expectedWater, testFlower.getWater());
        assertEquals(expectedNutrients, testFlower.getNutrient());
    }

    @AfterAll
    public static void tearDown() {
        simulationServiceMockedStatic.close();
    }
}