package com.gitlab.juli220620.service;

import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.service.delegates.AchievementsWithCountersProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {
    public static String MAMIN_SADOVOD_ID = "MAMIN_SADOVOD";
    public static String BOLSHIE_NADEZHDY_ID = "BOLSHIE_NADEZHDY";

    private final AchievementsWithCountersProcessor countersProcessor;
    @Transactional
    public void processMaminSadovod(UserEntity user, BaseFlowerDictEntity baseFlower) {
        countersProcessor.processAchievementWithCounter(user, baseFlower, MAMIN_SADOVOD_ID, 10);
    }

    @Transactional
    public void processBolshieNadezhdy(UserEntity user, BaseFlowerDictEntity baseFlower) {
        countersProcessor.processAchievementWithCounter(user, baseFlower, BOLSHIE_NADEZHDY_ID, 10);
    }
}
