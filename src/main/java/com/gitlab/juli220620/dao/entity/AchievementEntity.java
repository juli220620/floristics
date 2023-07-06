package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "achievement")
public class AchievementEntity {

    @Id
    private String id;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
