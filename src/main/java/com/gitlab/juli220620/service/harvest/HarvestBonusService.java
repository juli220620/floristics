package com.gitlab.juli220620.service.harvest;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HarvestBonusService {

    @Transactional
    public Map<String, HarvestBonusEntity> getHarvestBonuses(
            UserEntity user,
            BaseFlowerDictEntity baseFlower
    ) {
        if (user.getFlowerCount() == null) throw new RuntimeException();
        return getViableBonuses(baseFlower, user)
                .collect(Collectors.toMap(
                        e -> e.getCurrency().getId(),
                        e -> e,
                        (e1, e2) -> e1.getId().getCount() > e2.getId().getCount() ? e1 : e2
                ));
    }

    @Transactional
    public Optional<HarvestBonusEntity> getMaxOffspringChance(RoomFlowerEntity flower) {
        return getViableBonuses(flower.getBaseFlower(), flower.getRoom().getUser())
                .reduce((entity, entity2) ->
                        entity.getFreeOffspringChance() > entity2.getFreeOffspringChance()
                                ? entity
                                : entity2
                );
    }

    private Stream<HarvestBonusEntity> getViableBonuses(BaseFlowerDictEntity baseFlower, UserEntity user) {
        long flowerCount = user.getFlowerCount().get(baseFlower.getId());
        return baseFlower.getHarvestBonuses().stream()
                .filter(it -> it.getId().getCount() <= flowerCount);
    }
}
