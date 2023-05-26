package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import com.gitlab.juli220620.dao.entity.UserCurrencyEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.repo.CurrencyDictRepo;
import com.gitlab.juli220620.dao.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepo userRepo;
    private final CurrencyDictRepo currencyDictRepo;

    public boolean spend(Integer amount, String currencyId, UserEntity user) {
        UserCurrencyEntity entity = user.getWallet().stream()
                .filter(currency -> currency.getCurrencyId().contentEquals(currencyId))
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
                .filter(it -> it.getCurrencyId().contentEquals(currency.getId()))
                .findFirst().orElse(new UserCurrencyEntity(user.getId(), currency.getId(), 0));

        entity.setAmount(entity.getAmount() + amount);
        user.getWallet().add(entity);
        userRepo.save(user);
    }
}
