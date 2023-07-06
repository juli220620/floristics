package com.gitlab.juli220620.dao.entity;

import com.gitlab.juli220620.dao.entity.identity.UserAchievementId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.REFRESH;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_achievement")
public class UserAchievementEntity {

    @EmbeddedId
    private UserAchievementId id;

    private boolean achieved;
    private LocalDate date;

    @Column(columnDefinition = "JSON")
    private String meta;

    @MapsId("userId")
    @ManyToOne(optional = false, cascade = {MERGE, REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @MapsId("achievementId")
    @ManyToOne(optional = false, cascade = {MERGE, REFRESH})
    @JoinColumn(name = "achievement_id", referencedColumnName = "id")
    private AchievementEntity achievement;
}
