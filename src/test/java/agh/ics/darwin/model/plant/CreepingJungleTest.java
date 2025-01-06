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

}