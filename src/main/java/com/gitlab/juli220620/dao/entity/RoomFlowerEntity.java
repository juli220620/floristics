package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_flower")
public class RoomFlowerEntity {

    private Integer water;
    private Integer nutrient;
    private Long growth;
    private LocalDateTime updated;
    private LocalDateTime planted;
    private String status;
    private Long deathTicks;
    private Integer currentCycle;
    private Integer cycles;
    private boolean autoHarvest;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = { REFRESH, DETACH, MERGE, PERSIST }, fetch = LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private UserRoomEntity room;

    @ManyToOne(optional = false, cascade = { REFRESH, DETACH, MERGE })
    @JoinColumn(name = "flower_id", referencedColumnName = "id")
    private BaseFlowerDictEntity baseFlower;

    @ManyToOne(optional = false, cascade = { REFRESH, DETACH, MERGE })
    @JoinColumn(name = "pot_id", referencedColumnName = "id")
    private PotDictEntity basePot;
}
