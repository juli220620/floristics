package com.gitlab.juli220620.dao.entity;

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
@Table(name = "pot_dict")
public class PotDictEntity {

    @Id
    private String id;
    private String name;
    private Integer capacity;
    private Integer price;
    private Integer size;
}
