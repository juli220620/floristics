package com.gitlab.juli220620;

import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final BaseFlowerDictRepo flowerRepo;
    private final PotDictRepo potRepo;
    private final RoomFlowerRepo roomFlowerRepo;
    private final UserRepo userRepo;
    private final UserRoomRepo userRoomRepo;

    @GetMapping
    public Object test() {
        List<UserEntity> all = userRepo.getAll();
        return all;
    }

}
