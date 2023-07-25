package com.gitlab.juli220620.web.controllers.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantFlowerRq {

    private String baseFlowerId;
    private String potId;
    private Integer cycles;
    private boolean needFilling;
    private boolean autoHarvest;
}
