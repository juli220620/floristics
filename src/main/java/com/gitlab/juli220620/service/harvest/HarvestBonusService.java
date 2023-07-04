package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HarvestBonusService {

    @Transactional
    public Map<String, HarvestBonusEntity> getHarvestBonuses(
            UserEntity user,
            BaseFlowerDictEntity baseFlower) {

        if (user.getFlowerCount() == null) throw new RuntimeException();
        long flowerCount = user.getFlowerCount()
                .compute(baseFlower.getId(), (s, value) -> value == null ? 1 : value + 1);

        return baseFlower.getHarvestBonuses().stream()
                .filter(it -> it.getId().getCount() <= flowerCount)
                .collect(Collectors.toMap(
                        e -> e.getCurrency().getId(),
                        e -> e,
                        (e1, e2) -> e1.getId().getCount() > e2.getId().getCount() ? e1 : e2
                ));
    }
}
