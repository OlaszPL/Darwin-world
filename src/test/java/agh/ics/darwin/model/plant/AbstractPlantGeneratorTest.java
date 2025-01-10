package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPlantGeneratorTest {

    @Test
    void testGenerateWithPreferredFields() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        // when
        generator.generate(5);

        // then
        assertEquals(15, map.getPlantsSize());
    }

    @Test
    void testGenerateWithNoPlantsBefore() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new CreepingJungle(map, 0) {
        };

        // when
        generator.generate(5);

        // then
        assertEquals(5, map.getPlantsSize());
    }

    @Test
    void testGenerateWhenAllFieldsOccupied() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map.addPlant(new Plant(new Vector2d(i, j)));
            }
        }

        // when
        generator.generate(5);

        // then
        assertEquals(100, map.getPlantsSize());
    }

    @Test
    void testGenerateWithZeroPlants() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new CreepingJungle(map, 10) {
        };

        // when
        generator.generate(0);

        // then
        assertEquals(10, map.getPlantsSize());
    }

    @Test
    void testGenerateWithNegativePlants() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        AbstractPlantGenerator generator = new EquatorialForest(map, 10) {
        };

        // when
        generator.generate(-5);

        // then
        assertEquals(10, map.getPlantsSize());
    }
}