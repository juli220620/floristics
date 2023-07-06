package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    private String username;
    private String password;

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRoomEntity> userRooms;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserCurrencyEntity> wallet;

    @OneToMany(orphanRemoval = true, cascade = ALL, mappedBy = "user")
    private List<UserAchievementEntity> achievements;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
