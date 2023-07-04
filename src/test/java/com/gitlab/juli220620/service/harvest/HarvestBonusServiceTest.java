package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.identity.HarvestBonusEntityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HarvestBonusServiceTest {

    private final String CURRENCY_ID = "SOME_CURR_ID";
    @Spy
    private HarvestBonusService harvestBonusService;

    @Mock
    private UserEntity user;
    @Mock
    private BaseFlowerDictEntity baseFlower;

    @BeforeEach
    public void setup() {
        Mockito.doReturn("ANY_GENERIC_STRING").when(baseFlower).getId();
    }

    @Test
    public void getHarvestBonuses_whenNoAvailableBonuses_returnsEmptyMap() {
        setupUserFlowerCount(1L);
        createHarvestBonusEntity(10L, 2D, 100);

        Map<String, HarvestBonusEntity> bonuses = harvestBonusService.getHarvestBonuses(user, baseFlower);

        assertEquals(0, bonuses.size());
    }

    @Test
    public void getHarvestBonus_whenBonusAvailableAndCountViable_returnsBonus() {
        setupUserFlowerCount(10L);
        HarvestBonusEntity entity = createHarvestBonusEntity(10L, 2D, 100);

        Map<String, HarvestBonusEntity> bonuses = harvestBonusService.getHarvestBonuses(user, baseFlower);

        assertEquals(1, bonuses.size());
        assertTrue(bonuses.containsKey(CURRENCY_ID));
        HarvestBonusEntity resEntity = bonuses.get(CURRENCY_ID);

        assertNotNull(resEntity);
        assertEquals(entity, resEntity);
    }

    @Test
    public void getHarvestBonuses_whenMoreThanOneBonusAvailable_returnsHighest() {
        setupUserFlowerCount(100L);
        createHarvestBonusEntity(10L, 2D, 100);
        HarvestBonusEntity entity100 = createHarvestBonusEntity(100L, 3D, 200);

        Map<String, HarvestBonusEntity> bonuses = harvestBonusService.getHarvestBonuses(user, baseFlower);

        assertEquals(1, bonuses.size());
        assertTrue(bonuses.containsKey(CURRENCY_ID));
        HarvestBonusEntity resEntity = bonuses.get(CURRENCY_ID);

        assertNotNull(resEntity);
        assertEquals(entity100, resEntity);
    }

    private void setupUserFlowerCount(Long count) {
        Map<String, Long> values = Optional.ofNullable(user.getFlowerCount()).orElse(new HashMap<>());
        values.put(baseFlower.getId(), count);
        Mockito.doReturn(values).when(user).getFlowerCount();
    }

    private HarvestBonusEntity createHarvestBonusEntity(Long count, Double mul, Integer bonus) {
        HarvestBonusEntity mock = Mockito.mock(HarvestBonusEntity.class);
        HarvestBonusEntityId mockId = Mockito.mock(HarvestBonusEntityId.class);
        CurrencyDictEntity mockCurr = Mockito.mock(CurrencyDictEntity.class);

        Mockito.doReturn(baseFlower.getId()).when(mockId).getFlowerId();
        Mockito.doReturn(count).when(mockId).getCount();
        Mockito.doReturn(CURRENCY_ID).when(mockId).getCurrencyId();

        Mockito.doReturn(CURRENCY_ID).when(mockCurr).getId();
        Mockito.doReturn(mockCurr).when(mock).getCurrency();
        Mockito.doReturn(mul).when(mock).getMultiplier();
        Mockito.doReturn(bonus).when(mock).getFlatBonus();

        Mockito.doReturn(mockId).when(mock).getId();

        List<HarvestBonusEntity> entries = Optional.ofNullable(baseFlower.getHarvestBonuses())
                .orElse(new ArrayList<>());
        entries.add(mock);
        Mockito.doReturn(entries).when(baseFlower).getHarvestBonuses();

        return mock;
    }
}