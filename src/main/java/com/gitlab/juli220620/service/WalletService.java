package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.UserCurrencyEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.identity.UserCurrencyEntityId;
import com.gitlab.juli220620.dao.repo.CurrencyDictRepo;
import com.gitlab.juli220620.dao.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.CASH_ID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepo userRepo;
    private final CurrencyDictRepo currencyDictRepo;

    public boolean spend(Integer amount, String currencyId, UserEntity user) {
        UserCurrencyEntity entity = user.getWallet().stream()
                .filter(currency -> currency.getCurrency().getId().contentEquals(currencyId))
                .findFirst().orElse(null);
        if (entity == null || entity.getAmount() == null || entity.getAmount() < amount) return false;

        int initialAmount = entity.getAmount();
        entity.setAmount(initialAmount - amount);

        userRepo.save(user);
        return true;
    }

    public void receive(Integer amount, String currencyId, UserEntity user) {
        CurrencyDictEntity currency = currencyDictRepo.findById(currencyId)
                .orElseThrow(() -> new RuntimeException("No such currency"));
        receive(amount, currency, user);
    }

    public void receive(Integer amount, CurrencyDictEntity currency, UserEntity user) {
        UserCurrencyEntity entity = user.getWallet().stream()
                .filter(it -> it.getCurrency().getId().contentEquals(currency.getId()))
                .findFirst().orElse(new UserCurrencyEntity(new UserCurrencyEntityId(user.getId(), null), currency, 0));

        entity.setAmount(entity.getAmount() + amount);
        user.getWallet().add(entity);
        userRepo.save(user);
    }

    @Transactional
    public void sell(Integer amount, String currencyId, UserEntity user) {
        if (currencyId.equals(CASH_ID)) throw new RuntimeException("Can't sell cash");

        UserCurrencyEntity sold = user.getWallet().stream()
                .filter(it -> it.getCurrency().getId().equals(currencyId)).findFirst()
                .filter(it -> it.getAmount() >= amount)
                .orElseThrow(() -> new RuntimeException("Not enough currency"));

        UserCurrencyEntity cash = user.getWallet().stream()
                        .filter(it -> it.getCurrency().getId().equals(CASH_ID)).findFirst()
                .orElseThrow(() -> new RuntimeException("No cash"));

        cash.setAmount(cash.getAmount() + (amount * sold.getCurrency().getFactorToCash()));
        sold.setAmount(sold.getAmount() - amount);
    }
}
