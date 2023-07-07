package com.gitlab.juli220620.dao.entity.systems;

import java.util.Map;

public interface GameSystem {

    Map<Integer, Map<String, Integer>> getCost();

    String getId();
}
