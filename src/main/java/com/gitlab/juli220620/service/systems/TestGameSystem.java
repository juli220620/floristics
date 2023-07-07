package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.CurrencyDictEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestGameSystem implements GameSystem {
    public static final String ID = "TEST_SYSTEM";

    @Override
    public Map<Integer, Map<String, Integer>> getCost() {
        return Map.of(
                1, Map.of(CurrencyDictEntity.RED_ID, 100),
                2, Map.of(CurrencyDictEntity.RED_ID, 500)
        );
    }

    @Override
    public String getId() {
        return ID;
    }
}
