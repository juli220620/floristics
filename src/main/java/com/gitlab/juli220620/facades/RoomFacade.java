package com.gitlab.juli220620.facades;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import com.gitlab.juli220620.service.*;
import com.gitlab.juli220620.service.harvest.HarvestService;
import com.gitlab.juli220620.service.systems.PerennialFlowersGameSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.*;
import static com.gitlab.juli220620.service.TendingService.NUTRIENT_UNIT_COST;
import static com.gitlab.juli220620.service.TendingService.WATER_UNIT_COST;

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

    @Transactional
    public RoomFlowerEntity plantFlower(String token, String baseFlowerId, String potId, Integer cycles, Long roomId) {
        UserRoomEntity room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("No such room"));

        UserEntity user = loginService.findUserByToken(token);
        if (user.getUserRooms().stream().noneMatch(it -> Objects.equals(it.getId(), roomId)))
            throw new RuntimeException("Invalid room");

        RoomFlowerEntity entity = plantingService.plantFlower(baseFlowerId, potId, cycles, room);

        int amount = entity.getBaseFlower().getPrice() + entity.getBasePot().getPrice();
        int correctedAmount = perennialFlowersGameSystem.modifyPrice(amount, entity);

        if (!walletService.spend(correctedAmount, CASH_ID, user))
            throw new RuntimeException("Insufficient funds");

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
        RoomFlowerEntity entity = roomFlowerRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Invalid flower"));

        if (!loginService.checkUserToken(token, entity.getRoom().getUser()))
            throw new RuntimeException("Wrong flower");

        Map<String, Integer> harvest = harvestService.harvest(entity);

        harvest.forEach((currencyId, amount) ->
                walletService.receive(amount, currencyId, entity.getRoom().getUser()));
    }

    public UserRoomEntity getRoom(Long roomId, String token) {
        UserRoomEntity room = roomRepo.findById(roomId).orElseThrow(() -> new RuntimeException("Invalid room"));

        if (!loginService.checkUserToken(token, room.getUser()))
            throw new RuntimeException("That's not your room");

        return room;
    }

    private void tendFlower(String token, Long flowerId,
                            Integer unitCost, String currencyId,
                            Function<RoomFlowerEntity, Integer> strategy) {
        RoomFlowerEntity entity = roomFlowerRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Invalid flower"));

        if (!loginService.checkUserToken(token, entity.getRoom().getUser()))
            throw new RuntimeException("Access denied");

        Integer resultingAmount = strategy.apply(entity);

        if (resultingAmount == 0) return;

        if (!walletService.spend(resultingAmount * unitCost, currencyId, entity.getRoom().getUser()))
            throw new RuntimeException("Insufficient funds");
    }
}
