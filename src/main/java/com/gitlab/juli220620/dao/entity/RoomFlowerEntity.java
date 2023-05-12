package com.gitlab.juli220620.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomFlowerEntity {
    private Long id;
    private Integer water;
    private Integer nutrient;
    private Long growth;
    private LocalDateTime updated;
    private UserRoomEntity room;
    private BaseFlowerDictEntity baseFlower;
    private PotDictEntity basePot;
}
