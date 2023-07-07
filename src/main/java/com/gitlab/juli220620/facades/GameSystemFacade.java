package com.gitlab.juli220620.facades;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.UserGameSystemEntity;
import com.gitlab.juli220620.service.GameSystemService;
import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameSystemFacade {

    private final LoginService loginService;
    private final WalletService walletService;
    private final GameSystemService gameSystemService;

    @Transactional
    public void buyGameSystem(String token, String systemId) {
        UserEntity user = loginService.findUserByToken(token);

        Map<String, Integer> costs = gameSystemService.addSystem(user, systemId);

        boolean success = costs.entrySet().stream()
                        .allMatch(entry -> walletService.spend(entry.getValue(), entry.getKey(), user));
        if (!success) throw new RuntimeException("Not enough funds");
    }

    @Transactional
    public void upgradeGameSystem(String token, String systemId) {
        UserEntity user = loginService.findUserByToken(token);

        Map<String, Integer> costs = gameSystemService.upgradeSystem(user, systemId);

        boolean success = costs.entrySet().stream()
                .allMatch(payment -> walletService.spend(payment.getValue(), payment.getKey(), user));

        if (!success) throw new RuntimeException("Insufficient funds");
    }

    public List<UserGameSystemEntity> getUserSystems(String token) {
        return loginService.findUserByToken(token).getWorkingSystems();
    }
}
