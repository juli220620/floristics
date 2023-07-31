package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.*;
import com.gitlab.juli220620.dao.entity.identity.UserGameSystemId;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.facades.RoomFacade;
import com.gitlab.juli220620.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
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

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.GEMSTONES_ID;
import static com.gitlab.juli220620.service.systems.AutoHarvestGameSystem.GEMSTONES_FOR_AUTO_HARVEST;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AutoHarvestGameSystemTest {

    @Spy
    @InjectMocks
    private AutoHarvestGameSystem autoHarvestGameSystem;
    @Spy
    private RoomFlowerEntity flower;

    @Mock
    private RoomFlowerRepo roomFlowerRepo;
    @Mock
    private RoomFacade roomFacade;
    @Mock
    private WalletService walletService;
    @Mock
    private UserEntity user;
    @Mock
    private BaseFlowerDictEntity baseFlower;

    @BeforeEach
    public void setup() {
        UserRoomEntity room = Mockito.mock(UserRoomEntity.class);
        Mockito.doReturn(user).when(room).getUser();
        Mockito.doReturn(1).when(baseFlower).getPrice();

        flower.setBaseFlower(baseFlower);
        flower.setRoom(room);
    }

    @Test
    public void autoHarvest_whenCalled_happyPass() {
        autoHarvestGameSystem.autoHarvest(flower);
        Mockito.verify(roomFacade, Mockito.times(1)).harvestFlower(user, flower);
    }

    @Test
    public void processAutoHarvest_whenConditionsMet_setsAutoHarvest() {
        setSystem();
        Mockito.doReturn(Collections.emptyList()).when(roomFlowerRepo).findAllByAutoHarvest(user);
        Mockito.doReturn(true).when(walletService).spend(GEMSTONES_FOR_AUTO_HARVEST, GEMSTONES_ID, user);

        autoHarvestGameSystem.processAutoHarvest(user, flower);

        assertTrue(flower.isAutoHarvest());
    }

    @Test
    public void processAutoHarvest_whenCyclesNotNull_throwsException() {
        flower.setCycles(2);
        evaluateException("Not for perennial flowers");
    }

    @Test
    public void processAutoHarvest_whenFreeFlower_throwsException() {
        Mockito.doReturn(0).when(baseFlower).getPrice();
        evaluateException("Free flowers not included");
    }

    @Test
    public void processAutoHarvest_whenNoUserSystem_throwsException() {
        Mockito.doReturn(Collections.emptyList()).when(user).getWorkingSystems();
        evaluateException("You can't do that");
    }

    @Test
    public void processAutoHarvest_whenMaxFlowersFlagged_throwsException() {
        setSystem();
        Mockito.doReturn(List.of(flower))
                .when(roomFlowerRepo).findAllByAutoHarvest(user);
        evaluateException("Max number already reached");
    }

    @Test
    public void processAutoHarvest_whenNotEnoughGemstones_throwsException() {
        setSystem();
        Mockito.doReturn(Collections.emptyList()).when(roomFlowerRepo).findAllByAutoHarvest(user);
        Mockito.doReturn(false).when(walletService).spend(GEMSTONES_FOR_AUTO_HARVEST, GEMSTONES_ID, user);
        evaluateException("Faeries won't even look at your poor ass");
        Mockito.verify(
                walletService, Mockito.times(1))
                .spend(GEMSTONES_FOR_AUTO_HARVEST, GEMSTONES_ID, user);
    }

    private void evaluateException(String errorMessage) {
        Exception e = assertThrows(Exception.class, () -> autoHarvestGameSystem.processAutoHarvest(user, flower));
        assertEquals(errorMessage, e.getMessage());
    }
    private void setSystem() {
        UserGameSystemEntity userSystem = Mockito.mock(UserGameSystemEntity.class);
        UserGameSystemId systemId = Mockito.mock(UserGameSystemId.class);

        Mockito.doReturn(1).when(userSystem).getSystemLevel();
        Mockito.doReturn(autoHarvestGameSystem.getId()).when(systemId).getSystemId();
        Mockito.doReturn(systemId).when(userSystem).getId();
        Mockito.doReturn(List.of(userSystem)).when(user).getWorkingSystems();
    }
}