package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.*;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.gitlab.juli220620.service.SimulationService.GROWING_STATUS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RipeHarvestStrategyTest {
    private static final String CURRENCY1_ID = "CURRENCY1";
    private static final String CURRENCY2_ID = "CURRENCY2";
    private static final long FLOWER_ID = 1234;

    @Spy
    @InjectMocks
    private RipeHarvestStrategy strategy;
    @Spy
    private RoomFlowerEntity flower;
    @Mock
    private PotCashbackGameSystem potCashbackGameSystem;
    @Mock
    private AchievementService achievementService;
    @Mock
    private RoomFlowerRepo roomFlowerRepo;
    @Mock
    private HarvestBonusService bonusService;
    @Mock
    private BaseFlowerDictEntity baseFlower;
    @Mock
    private UserEntity user;

    @BeforeEach
    public void setup() {
        UserRoomEntity room = Mockito.mock(UserRoomEntity.class);

        flower.setId(FLOWER_ID);
        Map<String, Long> harvest = Map.of(CURRENCY1_ID, 100L, CURRENCY2_ID, 20L);
        flower.setRoom(room);
        Mockito.doReturn(user).when(room).getUser();
        Mockito.doReturn(harvest).when(baseFlower).getHarvest();
        Mockito.doReturn(1).when(baseFlower).getPrice();
        flower.setBaseFlower(baseFlower);
    }

    @ParameterizedTest
    @CsvSource({
            "300,260,"+CURRENCY1_ID+","+CURRENCY2_ID,
            "300,20,"+CURRENCY1_ID+",SOMETHING_ELSE",
            "100,20,SOMETHING_SOMETHING,SOMETHING_ELSE",
    })
    public void process_whenWithoutOffspringChance_flowerDeleted(long curr1, long curr2, String id1, String id2) {
        createBonuses(id1, id2);
        checkRes(curr1, curr2);
        Mockito.verify(roomFlowerRepo, Mockito.times(1)).customDelete(FLOWER_ID);
    }

    @Test
    public void postProcess_whenChanceForOffspring_UpdatesEntity() {
        HarvestBonusEntity bonus = new HarvestBonusEntity(
                null, baseFlower, null, 0.1, 2, 1.0);
        Mockito.doReturn(Optional.of(bonus)).when(bonusService).getMaxOffspringChance(flower);
        flower.setStatus("Something");
        flower.setGrowth(15L);
        flower.setDeathTicks(2L);

        strategy.postProcessFlower(flower);

        assertEquals(GROWING_STATUS, flower.getStatus());
        assertEquals(0L, flower.getGrowth());
        assertEquals(0L, flower.getDeathTicks());
        Mockito.verify(roomFlowerRepo, Mockito.times(0)).customDelete(FLOWER_ID);
    }

    private void checkRes(Long curr1, Long curr2) {
        Map<String, Long> results = strategy.process(flower);

        assertEquals(2, results.size());
        assertTrue(results.containsKey(CURRENCY1_ID));
        assertTrue(results.containsKey(CURRENCY2_ID));
        assertEquals(curr1, results.get(CURRENCY1_ID));
        assertEquals(curr2, results.get(CURRENCY2_ID));
    }

    private void createBonuses(String... curr) {
        Map<String, HarvestBonusEntity> bonuses = new HashMap<>();
        for (int i = 0; i < curr.length; i++) {
            String currId = curr[i];
            Double mul = (double) (2 + i);
            Integer flat = (1 + i) * 100;
            Double offspring = 0.;
            bonuses.put(currId, new HarvestBonusEntity(null, null, null, mul, flat, offspring));
        }
        Mockito.doReturn(bonuses)
                .when(bonusService).getHarvestBonuses(
                        Mockito.any(UserEntity.class),
                        Mockito.any(BaseFlowerDictEntity.class));
    }
}