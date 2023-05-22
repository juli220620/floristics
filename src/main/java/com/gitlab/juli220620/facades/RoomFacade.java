package com.gitlab.juli220620.facades;

import com.gitlab.juli220620.dao.entity.*;
import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.dao.repo.UserRoomRepo;
import com.gitlab.juli220620.service.LoginService;
import com.gitlab.juli220620.service.PlantingService;
import com.gitlab.juli220620.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomFacade {

    private final PlantingService plantingService;
    private final WalletService walletService;
    private final LoginService loginService;
    private final BaseFlowerDictRepo baseFlowerDictRepo;
    private final PotDictRepo potDictRepo;
    private final UserRoomRepo roomRepo;


    public RoomFlowerEntity plantFlower(String token, String baseFlowerId, String potId, Long roomId) {
        BaseFlowerDictEntity baseFlower = Optional.of(baseFlowerDictRepo.findById(baseFlowerId))
                .orElseThrow(() -> new RuntimeException("No such flower"));
        PotDictEntity pot = Optional.of(potDictRepo.findById(potId))
                .orElseThrow(() -> new RuntimeException("No such pot"));
        UserRoomEntity room = Optional.of(roomRepo.findById(roomId))
                .orElseThrow(() -> new RuntimeException("No such room"));

        UserEntity user = loginService.findUserByToken(token);

        if (user.getUserRooms().stream().noneMatch(it -> Objects.equals(it.getId(), roomId)))
            throw new RuntimeException("Invalid room");

        int amount = baseFlower.getPrice() + pot.getPrice();
        if (!walletService.spend(amount, "CASH", user))
            throw new RuntimeException("Insufficient funds");

        return plantingService.plantFlower(baseFlower, pot, room);
    }


}
