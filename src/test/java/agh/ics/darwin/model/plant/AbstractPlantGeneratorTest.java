package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPlantGeneratorTest {

    @Test
    void testGenerateWithPreferredFields() {
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        generator.generate(5);

        assertEquals(15, map.getPlantsSize());
    }

    @Test
    void testGenerateWithNoPlantsBefore() {
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new CreepingJungle(map, 0) {
        };

        generator.generate(5);

        assertEquals(5, map.getPlantsSize());
    }

    @Test
    void testGenerateWhenAllFieldsOccupied() {
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map.addPlant(new Plant(new Vector2d(i, j)));
            }
        }

        generator.generate(5);

        assertEquals(100, map.getPlantsSize());
    }

    @Test
    void testGenerateWithZeroPlants() {
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        generator.generate(0);

        assertEquals(10, map.getPlantsSize());
    }

    @Test
    void testGenerateWithNegativePlants() {
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        generator.generate(-5);

        assertEquals(10, map.getPlantsSize());
    }
}