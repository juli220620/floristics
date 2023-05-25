package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_room")
public class UserRoomEntity {

    private Integer area;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = LAZY, cascade = { DETACH, MERGE, REFRESH })
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
