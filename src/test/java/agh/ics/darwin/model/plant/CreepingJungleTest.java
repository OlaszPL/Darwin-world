package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.MapDirection;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreepingJungleTest {

    @Test
    void testGetPreferredFieldsWithNoPlants(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertTrue(preferredFields.isEmpty());
    }

    @Test
    void testGetPreferredFieldsWithPlant(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        Vector2d position = new Vector2d(5,5);
        map.addPlant(new Plant(position));
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(8, preferredFields.size());
        for (MapDirection direction : MapDirection.values()){
            assertTrue(preferredFields.contains(position.add(direction.toUnitVector())));
        }
        assertFalse(preferredFields.contains(new Vector2d(5, 7)));
    }

    @Test
    void testGetPreferredFieldsWithFullMap(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                map.addPlant(new Plant((new Vector2d(i, j))));
            }
        }

        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(0, preferredFields.size());
    }

    @Test
    void testGetPreferredFieldsWithSinglePlantAtEdge() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        Vector2d position = new Vector2d(0, 0);
        map.addPlant(new Plant(position));
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(3, preferredFields.size());
        assertTrue(preferredFields.contains(new Vector2d(0, 1)));
        assertTrue(preferredFields.contains(new Vector2d(1, 0)));
        assertTrue(preferredFields.contains(new Vector2d(1, 1)));
    }

    @Test
    void testGetPreferredFieldsWithMultiplePlants() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        map.addPlant(new Plant(new Vector2d(2, 2)));
        map.addPlant(new Plant(new Vector2d(5, 5)));
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(16, preferredFields.size());
        assertTrue(preferredFields.contains(new Vector2d(1, 2)));
        assertTrue(preferredFields.contains(new Vector2d(3, 2)));
        assertTrue(preferredFields.contains(new Vector2d(2, 1)));
        assertTrue(preferredFields.contains(new Vector2d(2, 3)));
        assertTrue(preferredFields.contains(new Vector2d(4, 5)));
        assertTrue(preferredFields.contains(new Vector2d(6, 5)));
        assertTrue(preferredFields.contains(new Vector2d(5, 4)));
        assertTrue(preferredFields.contains(new Vector2d(5, 6)));
    }

    @Test
    void testGetPreferredFieldsWithPlantsAtCorners() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        map.addPlant(new Plant(new Vector2d(0, 0)));
        map.addPlant(new Plant(new Vector2d(9, 9)));
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(6, preferredFields.size());
        assertTrue(preferredFields.contains(new Vector2d(0, 1)));
        assertTrue(preferredFields.contains(new Vector2d(1, 0)));
        assertTrue(preferredFields.contains(new Vector2d(1, 1)));
        assertTrue(preferredFields.contains(new Vector2d(8, 9)));
        assertTrue(preferredFields.contains(new Vector2d(9, 8)));
        assertTrue(preferredFields.contains(new Vector2d(8, 8)));
    }

    @Test
    void testGetPreferredFieldsWithPlantsInLine() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        for (int i = 0; i < 10; i++) {
            map.addPlant(new Plant(new Vector2d(i, 5)));
        }
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(20, preferredFields.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(preferredFields.contains(new Vector2d(i, 4)));
            assertTrue(preferredFields.contains(new Vector2d(i, 6)));
        }
    }

    @Test
    void testGetPreferredFieldsWithPlantsInSquare() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        for (int i = 3; i < 7; i++) {
            for (int j = 3; j < 7; j++) {
                map.addPlant(new Plant(new Vector2d(i, j)));
            }
        }
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(20, preferredFields.size());
        for (int i = 3; i < 7; i++) {
            assertTrue(preferredFields.contains(new Vector2d(i, 2)));
            assertTrue(preferredFields.contains(new Vector2d(i, 7)));
        }
        for (int j = 3; j < 7; j++) {
            assertTrue(preferredFields.contains(new Vector2d(2, j)));
            assertTrue(preferredFields.contains(new Vector2d(7, j)));
        }
    }

    @Test
    void testGetPreferredFieldsWithPlantsInCross() {
        // given
        EarthGlobeMap map = new EarthGlobeMap(10, 10);
        for (int i = 0; i < 10; i++) {
            map.addPlant(new Plant(new Vector2d(5, i)));
            map.addPlant(new Plant(new Vector2d(i, 5)));
        }
        CreepingJungle creepingJungle = new CreepingJungle(map, 0);

        // when
        List<Vector2d> preferredFields = creepingJungle.getPreferredFields();

        // then
        assertEquals(32, preferredFields.size());
        for (int i = 0; i < 10; i++) {
            if (i != 5) {
                assertTrue(preferredFields.contains(new Vector2d(4, i)));
                assertTrue(preferredFields.contains(new Vector2d(6, i)));
                assertTrue(preferredFields.contains(new Vector2d(i, 4)));
                assertTrue(preferredFields.contains(new Vector2d(i, 6)));
            }
        }
    }
}