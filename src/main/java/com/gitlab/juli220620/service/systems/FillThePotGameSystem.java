package com.gitlab.juli220620.service.systems;

import com.gitlab.juli220620.dao.entity.RoomFlowerEntity;
import com.gitlab.juli220620.service.TendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.gitlab.juli220620.dao.entity.CurrencyDictEntity.*;

@Service
@RequiredArgsConstructor
public class FillThePotGameSystem implements GameSystem {

    public static final String FILL_THE_POT_SYSTEM_ID = "FILL_THE_POT";

    private final TendingService tendingService;

    public void fillThePot(RoomFlowerEntity flower, int waterAmount, int nutrientAmount) {
        tendingService.water(waterAmount, flower);
        tendingService.feed(nutrientAmount, flower);
    }

    public Map<String, Integer> getFillingAmount(RoomFlowerEntity flower) {
        int waterConsumption = flower.getBaseFlower().getWaterConsumption();
        int nutrientConsumption = flower.getBaseFlower().getNutrientConsumption();
        double blueToGreenRatio = (double) waterConsumption /
                nutrientConsumption;
        int MaxWaterToAdd = (int) ((flower.getBasePot().getCapacity() * blueToGreenRatio) /
                (blueToGreenRatio + 1));
        int waterToAdd = (MaxWaterToAdd / waterConsumption) * waterConsumption;
        int nutrientToAdd = ((flower.getBasePot().getCapacity() - waterToAdd) /
                nutrientConsumption) * nutrientConsumption;

        return Map.of(
                BLUE_ID, waterToAdd,
                GREEN_ID,nutrientToAdd
        );
    }

    @Override
    public Map<Integer, Map<String, Long>> getCost() {
        return Map.of(1, Map.of(RED_ID, 20000L));
    }

    @Override
    public String getId() {
        return FILL_THE_POT_SYSTEM_ID;
    }
}
