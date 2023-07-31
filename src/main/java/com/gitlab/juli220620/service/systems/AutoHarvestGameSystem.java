package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.facades.RoomFacade;
import com.gitlab.juli220620.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.*;

@Component
@RequiredArgsConstructor
public class AutoHarvestGameSystem implements GameSystem {

    public static final String AUTO_HARVEST_ID = "AUTO_HARVEST";
    public static final Map<Integer, Integer> MAX_AMOUNT_AUTO_HARVESTED = Map.of(
            1, 1,
            2, 3,
            3, 5
    );

    public static final Long GEMSTONES_FOR_AUTO_HARVEST = 1L;

    private final RoomFlowerRepo roomFlowerRepo;

    @Lazy
    @Autowired
    private RoomFacade roomFacade;
    private final WalletService walletService;

    @Transactional
    public void autoHarvest(RoomFlowerEntity flower) {
        roomFacade.harvestFlower(flower.getRoom().getUser(), flower);
    }

    public void processAutoHarvest(UserEntity user, RoomFlowerEntity flower) {
        if (flower.getCycles() != null) throw new RuntimeException("Not for perennial flowers");
        if (flower.getBaseFlower().getPrice() == 0) throw new RuntimeException("Free flowers not included");

        UserGameSystemEntity userSystem = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(AUTO_HARVEST_ID))
                .findFirst()
                .orElse(null);

        if (userSystem == null) throw new RuntimeException("You can't do that");

        List<RoomFlowerEntity> alreadyAutoHarvested = roomFlowerRepo.findAllByAutoHarvest(user);

        if (alreadyAutoHarvested.size() >= MAX_AMOUNT_AUTO_HARVESTED.get(userSystem.getSystemLevel()))
            throw new RuntimeException("Max number already reached");

        if (!walletService.spend(GEMSTONES_FOR_AUTO_HARVEST, GEMSTONES_ID, user))
            throw new RuntimeException("Faeries won't even look at your poor ass");

        flower.setAutoHarvest(true);
    }

    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(
                1, Map.of(
                        CASH_ID, 200000L,
                        RED_ID, 50000L),
                2, Map.of(
                        RED_ID, 80000L),
                3, Map.of(
                        CASH_ID, 1000000L,
                        RED_ID, 600000L)
        );
    }

    @Override
    public String getId() {
        return AUTO_HARVEST_ID;
    }
}
