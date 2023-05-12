package com.gitlab.juli220620.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id;
    @Setter
    private String username;
    @Setter
    private String password;
    private List<UserRoomEntity> userRooms;
}
