package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepo userRepo;

    public boolean spend(Integer amount, String currencyId, UserEntity user) {
        Integer walletAmount = user.getWallet().get(currencyId);
        if (walletAmount == null || walletAmount < amount) return false;

        user.getWallet().put(currencyId, walletAmount - amount);
        userRepo.update(user);
        return true;
    }

    public void receive(Integer amount, String currencyId, UserEntity user) {
        user.getWallet().compute(currencyId, (s, integer) -> integer == null ? amount : integer + amount);
        userRepo.update(user);
    }
}
