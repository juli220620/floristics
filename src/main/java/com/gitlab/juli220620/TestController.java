package com.gitlab.juli220620;

import com.gitlab.juli220620.dao.repo.BaseFlowerDictRepo;
import com.gitlab.juli220620.dao.repo.PotDictRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final PotDictRepo potRepo;
    private final BaseFlowerDictRepo flowerRepo;

    @GetMapping
    public Object test() {
        return flowerRepo.findById("CACTUS");
    }

}
