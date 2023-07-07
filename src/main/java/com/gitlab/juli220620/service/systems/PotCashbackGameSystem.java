package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.PotDictEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.RED_ID;

@Component
@RequiredArgsConstructor
public class PotCashbackGameSystem implements GameSystem {

    public static final String ID = "POT_CASHBACK";
    public static final Map<Integer, Double> cashback = Map.of(
            1, .1,
            2, .11,
            3, .12
    );

    private final WalletService walletService;

    @Transactional
    public void cashback(UserEntity user, PotDictEntity pot) {
        UserGameSystemEntity entry = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(ID))
                .findFirst()
                .orElse(null);
        if (entry == null) return;

        walletService.receive(
                (int) (pot.getPrice() * cashback.get(entry.getSystemLevel())),
                CurrencyDictEntity.CASH_ID,
                user
        );
    }

    @Override
    public Map<Integer, Map<String, Integer>> getCost() {
        return Map.of(
                1, Map.of(RED_ID, 5000),
                2, Map.of(RED_ID, 80000),
                3, Map.of(RED_ID, 1000000)
        );
    }

    @Override
    public String getId() {
        return ID;
    }
}
