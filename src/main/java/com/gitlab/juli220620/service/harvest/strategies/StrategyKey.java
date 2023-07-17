package com.gitlab.juli220620.service.harvest.strategies;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static com.gitlab.juli220620.service.SimulationService.DEAD_STATUS;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class StrategyKey {
    private String status;
    private Boolean perennial;

    public StrategyKey(RoomFlowerEntity flower) {
        status = flower.getStatus();
        perennial = !flower.getStatus().contentEquals(DEAD_STATUS)
                ? flower.getCycles() != null
                : null;
    }
}
