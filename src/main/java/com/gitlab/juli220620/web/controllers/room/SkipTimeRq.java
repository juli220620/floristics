package com.gitlab.juli220620.web.controllers.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkipTimeRq {
    private Long flowerId;
    private Long ticksToSkip;
}