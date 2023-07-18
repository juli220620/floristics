package com.gitlab.juli220620.web.controllers.dict;

import com.gitlab.juli220620.dao.entity.HarvestBonusEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HarvestBonusDto {

    private String flowerId;
    private Long flowerCount;
    private String currencyId;

    private Double multiplier;
    private Integer flatBonus;

    public HarvestBonusDto(HarvestBonusEntity entity) {
        flowerId = entity.getId().getFlowerId();
        flowerCount = entity.getId().getCount();
        currencyId = entity.getId().getCurrencyId();

        multiplier = entity.getMultiplier();
        flatBonus = entity.getFlatBonus();
    }

}
