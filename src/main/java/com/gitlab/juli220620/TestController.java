package com.gitlab.juli220620;

import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import com.gitlab.juli220620.dao.repo.RoomFlowerRepo;
import com.gitlab.juli220620.dao.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final PotDictRepo potRepo;
    private final BaseFlowerDictRepo flowerRepo;
    private final RoomFlowerRepo roomFlowerRepo;
    private final UserRepo userRepo;

    @GetMapping("/{id}")
    public Object test(@PathVariable Long id) {
        return userRepo.findById(id);
    }

}
