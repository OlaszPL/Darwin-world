package agh.ics.darwin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void testRotate() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.rotate(0));
        assertEquals(MapDirection.NORTHEAST, MapDirection.NORTH.rotate(1));
        assertEquals(MapDirection.SOUTH, MapDirection.NORTH.rotate(4));
        assertThrows(IllegalArgumentException.class, () -> MapDirection.NORTH.rotate(-1));
        assertThrows(IllegalArgumentException.class, () -> MapDirection.NORTH.rotate(9));
    }

    @Test
    void testToUnitVector() {
        assertEquals(new Vector2d(0, 1), MapDirection.NORTH.toUnitVector());
        assertEquals(new Vector2d(1, 1), MapDirection.NORTHEAST.toUnitVector());
        assertEquals(new Vector2d(1, 0), MapDirection.EAST.toUnitVector());
        assertEquals(new Vector2d(1, -1), MapDirection.SOUTHEAST.toUnitVector());
        assertEquals(new Vector2d(0, -1), MapDirection.SOUTH.toUnitVector());
        assertEquals(new Vector2d(-1, -1), MapDirection.SOUTHWEST.toUnitVector());
        assertEquals(new Vector2d(-1, 0), MapDirection.WEST.toUnitVector());
        assertEquals(new Vector2d(-1, 1), MapDirection.NORTHWEST.toUnitVector());
    }

    @Test
    void testReverse() {
        assertEquals(MapDirection.SOUTH, MapDirection.NORTH.reverse());
        assertEquals(MapDirection.NORTH, MapDirection.SOUTH.reverse());
        assertEquals(MapDirection.WEST, MapDirection.EAST.reverse());
        assertEquals(MapDirection.EAST, MapDirection.WEST.reverse());
        assertEquals(MapDirection.SOUTHWEST, MapDirection.NORTHEAST.reverse());
        assertEquals(MapDirection.NORTHWEST, MapDirection.SOUTHEAST.reverse());
        assertEquals(MapDirection.SOUTHEAST, MapDirection.NORTHWEST.reverse());
        assertEquals(MapDirection.NORTHEAST, MapDirection.SOUTHWEST.reverse());
    }

    @Test
    void testGetRandomDirection() {
        for (int i = 0; i < 100; i++) {
            assertInstanceOf(MapDirection.class, MapDirection.getRandomDirection());
        }
    }

}