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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "base_flower_dict")
public class BaseFlowerDictEntity {

    @Id
    private String id;
    private String name;
    private Long growthTime;
    private Integer waterConsumption;
    private Integer nutrientConsumption;
    private Integer price;

    @ElementCollection
    @CollectionTable(name = "flower_harvest",
            joinColumns = {@JoinColumn(name = "flower_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "currency_id")
    @Column(name = "amount")
    private Map<String, Integer> harvest;

    @OneToMany(mappedBy = "baseFlower")
    private List<HarvestBonusEntity> harvestBonuses;
}
