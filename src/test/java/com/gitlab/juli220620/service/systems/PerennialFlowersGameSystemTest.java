package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.entity.identity.UserGameSystemId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PerennialFlowersGameSystemTest {

    @Spy
    private PerennialFlowersGameSystem perennialFlowersGameSystem;
    @Spy
    private RoomFlowerEntity flower;

    @Mock
    private BaseFlowerDictEntity baseFlower;
    @Mock
    private UserEntity user;

    @BeforeEach
    public void setup() {
        UserGameSystemId id = Mockito.mock(UserGameSystemId.class);
        UserGameSystemEntity system = Mockito.mock(UserGameSystemEntity.class);

        Mockito.doReturn(perennialFlowersGameSystem.getId()).when(id).getSystemId();
        Mockito.doReturn(id).when(system).getId();
        Mockito.doReturn(new ArrayList<>(List.of(system))).when(user).getWorkingSystems();
        Mockito.doReturn(1).when(system).getSystemLevel();
        Mockito.doReturn(2).when(baseFlower).getMaxCycles();
        Mockito.doReturn(baseFlower).when(flower).getBaseFlower();

    }


    @Test
    public void processPerennialFlower_whenSystemActive_happyPass() {
        perennialFlowersGameSystem.processPerennialFlower(user, flower, 2);


        assertEquals(2, flower.getCycles());
        assertEquals(1, flower.getCurrentCycle());
    }

    @ParameterizedTest
    @CsvSource(value = {"1", "null"}, nullValues = "null")
    public void processPerennialFlower_whenSystemActiveAndWrongCyclesPassed_noChanges(Integer cycles) {
        perennialFlowersGameSystem.processPerennialFlower(user, flower, cycles);

        assertNull(flower.getCycles());
        assertNull(flower.getCurrentCycle());
    }

    @Test
    public void processPerennialFlower_whenNotActiveSystem_error() {
        user.getWorkingSystems().clear();

        Exception e = assertThrows(Exception.class, () -> perennialFlowersGameSystem
                .processPerennialFlower(user, flower, 2));

        assertEquals("You can't do that.", e.getMessage());
    }

    @Test
    public void processPerennialFlower_whenTooManyCycles_error() {
        Exception e = assertThrows(Exception.class, () -> perennialFlowersGameSystem
                .processPerennialFlower(user, flower, 3));

        assertEquals("Too many cycles", e.getMessage());
    }

    @Test
    public void modifyPrice_whenCorrectValues_happyPass() {
        flower.setCycles(2);
        assertEquals(102, perennialFlowersGameSystem.modifyPrice(100, flower));
    }

    @Test
    public void modifyPrice_whenFlowerNull_amountUnchanged() {
        assertEquals(100, perennialFlowersGameSystem.modifyPrice(100, null));
    }

    @Test
    public void modifyPrice_whenCyclesNull_amountUnchanged() {
        assertEquals(100, perennialFlowersGameSystem.modifyPrice(100, flower));
    }

    @Test
    public void modifyPrice_whenInvalidCycles_amountUnchanged() {
        flower.setCycles(1);
        assertEquals(100, perennialFlowersGameSystem.modifyPrice(100, flower));
    }
}
