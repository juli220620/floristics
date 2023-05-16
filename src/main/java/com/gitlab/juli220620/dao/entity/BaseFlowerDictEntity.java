package com.gitlab.juli220620.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseFlowerDictEntity {

    private String id;
    private String name;
    private Long growthTime;
    private Integer waterConsumption;
    private Integer nutrientConsumption;
    private Map<String, Integer> harvest;

}
