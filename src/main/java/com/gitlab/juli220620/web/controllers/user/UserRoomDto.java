package com.gitlab.juli220620.web.controllers.user;

import com.gitlab.juli220620.dao.entity.UserRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomDto {

    private Long id;
    private String name;

    public UserRoomDto(UserRoomEntity room) {
        id = room.getId();
        name = room.getName();
    }
}
