package agh.ics.darwin.model.plant;

import agh.ics.darwin.model.EarthGlobeMap;
import agh.ics.darwin.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquatorialForestTest {

    @Test
    void testGetPreferredFieldsEvenRowsWithoutPlants(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(16, 16);
        EquatorialForest equatorialForest = new EquatorialForest(map, 0);

        // when
        List<Vector2d> preferredFields = equatorialForest.getPreferredFields();

        // then
        assertEquals(64, preferredFields.size());
        assertTrue(preferredFields.contains(new Vector2d(2, 7)));
        assertTrue(preferredFields.contains(new Vector2d(5, 8)));
        assertTrue(preferredFields.contains(new Vector2d(8, 9)));
        assertTrue(preferredFields.contains(new Vector2d(10, 6)));
        assertFalse(preferredFields.contains(new Vector2d(0, 5)));
        assertFalse(preferredFields.contains(new Vector2d(0, 10)));
    }

    @Test
    void testGetPreferredFieldsOddRowsWithoutPlants(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(11, 11);
        EquatorialForest equatorialForest = new EquatorialForest(map, 0);

        // when
        List<Vector2d> preferredFields = equatorialForest.getPreferredFields();

        // then
        assertEquals(33, preferredFields.size());
        assertTrue(preferredFields.contains(new Vector2d(2, 5)));
        assertTrue(preferredFields.contains(new Vector2d(5, 6)));
        assertTrue(preferredFields.contains(new Vector2d(8, 4)));
        assertFalse(preferredFields.contains(new Vector2d(0, 3)));
        assertFalse(preferredFields.contains(new Vector2d(0, 7)));
    }

    @Test
    void testGetPreferredFieldsWithExistingPlants(){
        // given
        EarthGlobeMap map = new EarthGlobeMap(12, 12);
        map.addPlant(new Plant(new Vector2d(0, 5)));
        EquatorialForest equatorialForest = new EquatorialForest(map, 0);

        // when
        List<Vector2d> preferredFields = equatorialForest.getPreferredFields();

        // then
        assertFalse(preferredFields.contains(new Vector2d(0, 5)));
    }

}