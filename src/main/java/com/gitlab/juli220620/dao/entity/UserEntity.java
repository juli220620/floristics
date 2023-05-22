package com.gitlab.juli220620.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private String username;
    private String password;

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRoomEntity> userRooms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_currency", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id"),
    })
    @MapKeyColumn(name = "currency_id")
    @Column(name = "amount")
    private Map<String, Integer> wallet;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
