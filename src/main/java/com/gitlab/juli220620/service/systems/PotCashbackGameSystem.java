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

    public static final String POT_CASHBACK_SYSTEM_ID = "POT_CASHBACK";
    public static final Map<Integer, Double> cashback = Map.of(
            1, .1,
            2, .11,
            3, .12
    );

    private final WalletService walletService;

    @Transactional
    public void cashback(UserEntity user, PotDictEntity pot) {
        UserGameSystemEntity entry = user.getWorkingSystems().stream()
                .filter(it -> it.getId().getSystemId().contentEquals(POT_CASHBACK_SYSTEM_ID))
                .findFirst()
                .orElse(null);
        if (entry == null) return;

        walletService.receive(
                (long) (pot.getPrice() * cashback.get(entry.getSystemLevel())),
                CurrencyDictEntity.CASH_ID,
                user
        );
    }

    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(
                1, Map.of(RED_ID, 5000L),
                2, Map.of(RED_ID, 80000L),
                3, Map.of(RED_ID, 1000000L)
        );
    }

    @Override
    public String getId() {
        return POT_CASHBACK_SYSTEM_ID;
    }
}
