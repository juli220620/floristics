package com.gitlab.juli220620.service.delegates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.juli220620.dao.entity.BaseFlowerDictEntity;
import com.gitlab.juli220620.dao.entity.UserAchievementEntity;
import com.gitlab.juli220620.dao.entity.UserEntity;
import com.gitlab.juli220620.dao.entity.identity.UserAchievementId;
import com.gitlab.juli220620.dao.repo.AchievementRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementsWithCountersProcessor {

    private final AchievementRepo achievementRepo;
    private final ObjectMapper objectMapper;

    public void processAchievementWithCounter(UserEntity user,
                                              BaseFlowerDictEntity baseFlower,
                                              String achievementId,
                                              int counter) {
        if (baseFlower.getPrice() <= 0) return;

        UserAchievementEntity userAchievement = user.getAchievements().stream()
                .filter(it -> it.getId().getAchievementId().equals(achievementId))
                .findFirst()
                .orElseGet(() -> getAchievement(user, achievementId));
        if (userAchievement.isAchieved()) return;

        try {
            if (userAchievement.getMeta() == null) {
                userAchievement.setMeta(objectMapper.writeValueAsString(new CounterMeta()));
            }
            CounterMeta meta = objectMapper.readValue(userAchievement.getMeta(), CounterMeta.class);
            meta.count++;
            if (meta.count >= counter) {
                userAchievement.setAchieved(true);
                userAchievement.setDate(LocalDate.now());
            }
            userAchievement.setMeta(objectMapper.writeValueAsString(meta));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private UserAchievementEntity getAchievement(UserEntity user, String id) {
        UserAchievementEntity entity = new UserAchievementEntity(
                new UserAchievementId(),
                false, null, null,
                user,
                achievementRepo.findById(id).orElseThrow());

        user.getAchievements().add(entity);
        return entity;
    }

    @Data
    private static class CounterMeta {
        private int count = 0;
    }
}
