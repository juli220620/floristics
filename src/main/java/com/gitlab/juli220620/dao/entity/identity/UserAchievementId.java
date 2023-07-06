package com.gitlab.juli220620.dao.entity.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementId implements Serializable {
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    @Column(name = "achievement_id", insertable = false, updatable = false)
    private String achievementId;
}
