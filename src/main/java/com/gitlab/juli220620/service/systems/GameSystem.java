package com.gitlab.juli220620.service.systems;

import java.util.Map;

public interface GameSystem {

    Map<Integer, Map<String, Long>> getCost();

    String getId();
}
