package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.*;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.service.AchievementService;
import com.gitlab.juli220620.service.harvest.HarvestBonusService;
import com.gitlab.juli220620.service.systems.PotCashbackGameSystem;
import org.junit.jupiter.api.BeforeEach;
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
    RipeHarvestStrategy strategy;
    @Mock
    private RoomFlowerEntity flower;
    @Mock
    private PotCashbackGameSystem potCashbackGameSystem;
    @Mock
    private AchievementService achievementService;
    @Mock
    RoomFlowerRepo roomFlowerRepo;
    @Mock
    HarvestBonusService bonusService;

    @BeforeEach
    public void setup() {
        UserEntity user = Mockito.mock(UserEntity.class);
        UserRoomEntity room = Mockito.mock(UserRoomEntity.class);
        BaseFlowerDictEntity baseFlower = Mockito.mock(BaseFlowerDictEntity.class);

        Mockito.doReturn(FLOWER_ID).when(flower).getId();
        Map<String, Integer> harvest = Map.of(CURRENCY1_ID, 100, CURRENCY2_ID, 20);
        Mockito.doReturn(room).when(flower).getRoom();
        Mockito.doReturn(user).when(room).getUser();
        Mockito.doReturn(harvest).when(baseFlower).getHarvest();
        Mockito.doReturn(baseFlower).when(flower).getBaseFlower();
    }

    @ParameterizedTest
    @CsvSource({
            "300,260,"+CURRENCY1_ID+","+CURRENCY2_ID,
            "300,20,"+CURRENCY1_ID+",SOMETHING_ELSE",
            "100,20,SOMETHING_SOMETHING,SOMETHING_ELSE",
    })
    public void process_whenCalled_happyPass(int curr1, int curr2, String id1, String id2) {
        createBonuses(id1, id2);
        checkRes(curr1, curr2);
        Mockito.verify(roomFlowerRepo, Mockito.times(1)).customDelete(FLOWER_ID);
    }

    private void checkRes(Integer curr1, Integer curr2) {
        Map<String, Integer> results = strategy.process(flower);

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
            bonuses.put(currId, new HarvestBonusEntity(null, null, null, mul, flat));
        }
        Mockito.doReturn(bonuses)
                .when(bonusService).getHarvestBonuses(
                        Mockito.any(UserEntity.class),
                        Mockito.any(BaseFlowerDictEntity.class));
    }
}