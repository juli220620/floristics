package com.gitlab.juli220620.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomFlowerEntity {
    private Long id;
    private Integer water;
    private Integer nutrient;
    private Long growth;
    private LocalDateTime updated;
    private String status;
    @JsonIgnore
    private UserRoomEntity room;
    private BaseFlowerDictEntity baseFlower;
    private PotDictEntity basePot;
}
