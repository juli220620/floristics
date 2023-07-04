package com.gitlab.juli220620.dao.entity;

import com.gitlab.juli220620.dao.entity.identity.HarvestBonusEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flower_harvest_bonus_info")
public class HarvestBonusEntity {

    @EmbeddedId
    private HarvestBonusEntityId id;

    @MapsId("flowerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "flower_id", referencedColumnName = "id")
    private BaseFlowerDictEntity baseFlower;

    @MapsId("currencyId")
    @ManyToOne(fetch = LAZY)
    private CurrencyDictEntity currency;

    private Double multiplier;
    private Integer flatBonus;
}
