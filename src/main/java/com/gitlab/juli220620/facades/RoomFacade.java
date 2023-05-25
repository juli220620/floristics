package com.gitlab.juli220620.facades;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.service.PlantingService;
import com.gitlab.juli220620.service.TendingService;
import com.gitlab.juli220620.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserRoomRepo roomRepo;
    private final RoomFlowerRepo roomFlowerRepo;

    @Transactional
    public RoomFlowerEntity plantFlower(String token, String baseFlowerId, String potId, Long roomId) {
        UserRoomEntity room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("No such room"));

        UserEntity user = loginService.findUserByToken(token);
        if (user.getUserRooms().stream().noneMatch(it -> Objects.equals(it.getId(), roomId)))
            throw new RuntimeException("Invalid room");

        RoomFlowerEntity entity = plantingService.plantFlower(baseFlowerId, potId, room);

        int amount = entity.getBaseFlower().getPrice() + entity.getBasePot().getPrice();
        if (!walletService.spend(amount, CASH_ID, user))
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
