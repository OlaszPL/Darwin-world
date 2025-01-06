package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {

    @Test
    void checkIfPlantInitializedCorrectly(){
        Vector2d position = new Vector2d(3, 3);
        Plant plant = new Plant(position);
        assertInstanceOf(Plant.class, plant);
        assertEquals(position, plant.getPosition());
    }

}