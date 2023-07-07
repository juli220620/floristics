package com.gitlab.juli220620.dao.entity;

import com.gitlab.juli220620.dao.entity.identity.UserGameSystemId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_game_system")
public class UserGameSystemEntity {

    @EmbeddedId
    private UserGameSystemId id;

    private Integer systemLevel;

    @MapsId("systemId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    private GameSystemEntity system;
}
