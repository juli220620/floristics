package com.gitlab.juli220620.web.controllers.room;

import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomStateDto {

    private Long id;
    private String name;

    private List<FlowerStateDto> flowers;
    private Integer area;
    private String serverTime;

    public RoomStateDto(UserRoomEntity room) {
        id = room.getId();
        name = room.getName();
        area = room.getArea();

        flowers = room.getFlowers().stream()
                .map(FlowerStateDto::new)
                .collect(Collectors.toList());

        serverTime = LocalDateTime.now().format(FlowerStateDto.formatter);
    }

}
