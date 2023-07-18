package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.entity.PotDictEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PotDto {

    private String id;
    private String name;
    private Integer capacity;
    private Integer price;

    public PotDto(PotDictEntity pot) {
        id = pot.getId();
        name = pot.getName();
        capacity = pot.getCapacity();
        price = pot.getPrice();
    }
}
