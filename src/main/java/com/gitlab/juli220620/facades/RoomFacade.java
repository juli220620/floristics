package com.gitlab.juli220620.facades;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.service.PlantingService;
import com.gitlab.juli220620.service.TendingService;
import com.gitlab.juli220620.service.WalletService;
import com.gitlab.juli220620.service.harvest.HarvestService;
import com.gitlab.juli220620.service.systems.FillThePotGameSystem;
import com.gitlab.juli220620.service.systems.AutoHarvestGameSystem;
import com.gitlab.juli220620.service.systems.PerennialFlowersGameSystem;
import com.gitlab.juli220620.service.systems.RoomEnlargementSystem;
import com.gitlab.juli220620.service.systems.TimeSkipGameSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.*;
import static com.gitlab.juli220620.service.TendingService.NUTRIENT_UNIT_COST;
import static com.gitlab.juli220620.service.TendingService.WATER_UNIT_COST;
import static com.gitlab.juli220620.service.systems.RoomEnlargementSystem.STONES_FOR_ROOM_ENLARGEMENT;
import static com.gitlab.juli220620.service.systems.FillThePotGameSystem.FILL_THE_POT_SYSTEM_ID;
import static com.gitlab.juli220620.service.systems.TimeSkipGameSystem.TIME_SKIP_SYSTEM_ID;

@Service
@RequiredArgsConstructor
public class RoomFacade {

    private final PlantingService plantingService;
    private final WalletService walletService;
    private final LoginService loginService;
    private final TendingService tendingService;
    private final HarvestService harvestService;

    private final UserRoomRepo roomRepo;
    private final RoomFlowerRepo roomFlowerRepo;

    private final PerennialFlowersGameSystem perennialFlowersGameSystem;
    private final TimeSkipGameSystem timeSkipGameSystem;
    private final FillThePotGameSystem fillThePotGameSystem;
    private final RoomEnlargementSystem roomEnlargementSystem;

    @Lazy
    @Autowired
    private AutoHarvestGameSystem autoHarvestGameSystem;


    @Transactional
    public RoomFlowerEntity plantFlower(
            String token,
            String baseFlowerId,
            String potId,
            Integer cycles,
            boolean autoHarvest,
            boolean needFilling,
            Long roomId
    ) {
        UserRoomEntity room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("No such room"));

        UserEntity user = loginService.findUserByToken(token);
        if (user.getUserRooms().stream().noneMatch(it -> Objects.equals(it.getId(), roomId)))
            throw new RuntimeException("Invalid room");

        RoomFlowerEntity entity = plantingService.plantFlower(baseFlowerId, potId, cycles, room);

        long amount = entity.getBaseFlower().getPrice() + entity.getBasePot().getPrice();
        long correctedAmount = perennialFlowersGameSystem.modifyPrice(amount, entity);

        if (!walletService.spend(correctedAmount, CASH_ID, user))
            throw new RuntimeException("Insufficient funds");

        if (needFilling && hasWorkingSystem(user, FILL_THE_POT_SYSTEM_ID)) {
            Map<String, Integer> filling = fillThePotGameSystem.getFillingAmount(entity);
            if (walletService.spend((long) filling.get(BLUE_ID) * WATER_UNIT_COST, BLUE_ID, user)
                    && walletService.spend((long) filling.get(GREEN_ID) * NUTRIENT_UNIT_COST, GREEN_ID, user)) {
                fillThePotGameSystem.fillThePot(entity, filling.get(BLUE_ID), filling.get(GREEN_ID));
            }
        }

        if (autoHarvest) autoHarvestGameSystem.processAutoHarvest(user, entity);

        return entity;
    }

    @Transactional
    public void feedFlower(String token, Long flowerId, Integer amount) {
        tendFlower(token, flowerId, NUTRIENT_UNIT_COST, GREEN_ID, entity -> tendingService.feed(amount, entity));
    }

    @Transactional
    public void waterFlower(String token, Long flowerId, Integer amount) {
        tendFlower(token, flowerId, WATER_UNIT_COST, BLUE_ID, entity -> tendingService.water(amount, entity));
    }

    @Transactional
    public void harvestFlower(String token, Long flowerId) {
        RoomFlowerEntity entity = validateUserFlower(token, flowerId);
        harvestFlower(entity.getRoom().getUser(), entity);
    }

    @Transactional
    public void harvestFlower(UserEntity user, RoomFlowerEntity flower) {
        harvestService.harvest(flower).forEach((currencyId, amount) ->
                walletService.receive(amount, currencyId, user));
    }

    public UserRoomEntity getRoom(Long roomId, String token) {
        UserRoomEntity room = roomRepo.findById(roomId).orElseThrow(() -> new RuntimeException("Invalid room"));

        if (!loginService.checkUserToken(token, room.getUser()))
            throw new RuntimeException("That's not your room");

        return room;
    }

    public RoomFlowerEntity getFlower(Long roomId, Long flowerId, String token) {
        UserRoomEntity room = getRoom(roomId, token);
        return room.getFlowers().stream()
                .filter(it -> it.getId().equals(flowerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No such flower here"));
    }

    @Transactional
    public void skipTime(String token, Long flowerId, Long ticksToSkip) {
        RoomFlowerEntity entity = validateUserFlower(token, flowerId);
        UserEntity user = entity.getRoom().getUser();

        UserGameSystemEntity userGameSystemEntity = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(TIME_SKIP_SYSTEM_ID)).findFirst()
                .orElseThrow(() -> new RuntimeException("No time skipping for you!"));

        long price = timeSkipGameSystem.calculatePrice(userGameSystemEntity, ticksToSkip);

        if (!walletService.spend(price, DELORIAN_ID, user))
            throw new RuntimeException("You're not ready to time travel this far");

        timeSkipGameSystem.skipTime(entity, ticksToSkip);
    }

    @Transactional
    public void enlargeRoom(String token, Long roomId) {
        UserRoomEntity room = getRoom(roomId, token);
        if (!walletService.spend(STONES_FOR_ROOM_ENLARGEMENT, STONES_AND_BOARDS_ID, room.getUser()))
            throw new RuntimeException("You don't have enough materials for that");
        roomEnlargementSystem.enlargeRoom(room);
    }

    private void tendFlower(String token, Long flowerId,
                            Integer unitCost, String currencyId,
                            Function<RoomFlowerEntity, Integer> strategy) {
        RoomFlowerEntity entity = validateUserFlower(token, flowerId);

        Integer resultingAmount = strategy.apply(entity);

        if (resultingAmount == 0) return;

        if (!walletService.spend((long) resultingAmount * unitCost, currencyId, entity.getRoom().getUser()))
            throw new RuntimeException("Insufficient funds");
    }

    private RoomFlowerEntity validateUserFlower(String token, Long flowerId) {
        RoomFlowerEntity entity = roomFlowerRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Invalid flower"));

        if (!loginService.checkUserToken(token, entity.getRoom().getUser()))
            throw new RuntimeException("Access denied");

        return entity;
    }

    private boolean hasWorkingSystem(UserEntity user, String systemId) {
        return user.getWorkingSystems().stream()
                .anyMatch(it -> it.getId().getSystemId().contentEquals(systemId));
    }
}